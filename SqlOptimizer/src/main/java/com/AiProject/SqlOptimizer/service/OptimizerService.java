package com.AiProject.SqlOptimizer.service;

import com.AiProject.SqlOptimizer.dto.MetadataExtraction;
import com.AiProject.SqlOptimizer.dto.OptimizationResponseDTO;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class OptimizerService {

    private final ParserService parserService;
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    @Value("${spring.ai.ollama.chat.options.model}")
    private String model;

    public OptimizerService(ParserService parserService, ChatClient chatClient, ObjectMapper objectMapper) {
        this.parserService = parserService;
        this.chatClient = chatClient;
        this.objectMapper = objectMapper;
    }

    public OptimizationResponseDTO optimizeQuery(String rawSql) {
        // print rawSql for debugging purposes
        System.out.println("model: " + model);
        // Step 5 Structural Verification & Extractions
        MetadataExtraction metadata = parserService.extractMetadata(rawSql);

        var converter = new BeanOutputConverter<>(new ParameterizedTypeReference<OptimizationResponseDTO>() {});

        String structuredPrompt = """
                Analyze the following SQL Query based on its parsed runtime details. Raw Query: %s Target Tables Involved: %s Extracted Joins: %s Filter Filters/Conditions: %s Generate optimization strategies, provide missing indices strategies, analyze explicit joins for bottlenecks, and return an optimized execution layout format. Format instructions: %s
            """.formatted(rawSql, metadata.getTables(), metadata.getJoinConditions(), metadata.getWhereClauses(), converter.getFormat());

        ChatResponse chatResponse = chatClient.prompt()
                .user(structuredPrompt)
                .call()
                .chatResponse();

//        String rawContent = chatResponse.getResult().getOutput().getContent();
          String rawContent = chatResponse.getResult().getOutput().getText();

          // Print rawContent for debugging purposes
        System.out.println("Raw Content from Chat Response: " + rawContent);

        // Manual cleaning execution line for low parameter models (0.5b occasionally leaks markdown tags)
        if (rawContent.contains("```json")) {
            rawContent = rawContent.substring(rawContent.indexOf("```json") + 7);
            rawContent = rawContent.substring(0, rawContent.lastIndexOf("```"));
        }

        OptimizationResponseDTO responseDTO = converter.convert(rawContent.trim());
        responseDTO.setOriginalQuery(rawSql);

        // Map Usage token details for monitoring
        OptimizationResponseDTO.PerformanceMetrics metrics = new OptimizationResponseDTO.PerformanceMetrics();
        var usage = chatResponse.getMetadata().getUsage();
        metrics.setPromptTokens(usage.getPromptTokens());
        metrics.setGenerationTokens(usage.getCompletionTokens());
        metrics.setTotalTokens(usage.getTotalTokens());
        metrics.setModelUsed(model);

        responseDTO.setPerformanceMetrics(metrics);

        return responseDTO;
    }
}
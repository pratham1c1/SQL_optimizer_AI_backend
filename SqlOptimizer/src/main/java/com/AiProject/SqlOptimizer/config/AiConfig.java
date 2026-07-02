package com.AiProject.SqlOptimizer.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("""
                You are an elite PostgreSQL Database Tuning Engineer.
                Your job is to analyze SQL queries alongside their structural metadata and provide optimizations.
                
                CRITICAL INSTRUCTION:
                You must output raw JSON strings that exactly conform to the structural layout requested by the user's template wrapper.
                Do not include markdown blocks like ```json or trailing text conversation. 
                Keep optimizations highly deterministic for qwen2.5:0.5b execution constraints.
                """)
                .build();
    }
}

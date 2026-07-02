package com.AiProject.SqlOptimizer.dto;

import lombok.Data;
import java.util.List;

@Data
public class OptimizationResponseDTO {
    private String originalQuery;
    private String optimizedQuery;
    private List<String> analysisFindings;
    private List<String> indexRecommendations;
    private List<String> joinOptimizations;
    private PerformanceMetrics performanceMetrics;

    @Data
    public static class PerformanceMetrics {
        private long promptTokens;
        private long generationTokens;
        private long totalTokens;
        private String modelUsed;
    }
}
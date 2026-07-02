package com.AiProject.SqlOptimizer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataExtraction {
    private boolean isValid;
    private String queryType;
    private List<String> tables;
    private List<String> joinConditions;
    private List<String> whereClauses;
}
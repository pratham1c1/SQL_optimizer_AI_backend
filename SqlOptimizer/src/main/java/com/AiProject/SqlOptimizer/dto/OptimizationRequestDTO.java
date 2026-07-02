package com.AiProject.SqlOptimizer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptimizationRequestDTO {

    @NotBlank(message = "SQL raw query string cannot be empty or null.")
    private String sqlQuery;
}
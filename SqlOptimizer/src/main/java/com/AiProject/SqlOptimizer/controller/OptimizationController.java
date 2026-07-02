package com.AiProject.SqlOptimizer.controller;

import com.AiProject.SqlOptimizer.dto.OptimizationRequestDTO;
import com.AiProject.SqlOptimizer.dto.OptimizationResponseDTO;
import com.AiProject.SqlOptimizer.service.OptimizerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/sql")
public class OptimizationController {

    private final OptimizerService optimizerService;

    public OptimizationController(OptimizerService optimizerService) {
        this.optimizerService = optimizerService;
    }

    @PostMapping("/optimize")
    public ResponseEntity<OptimizationResponseDTO> optimize(
            @Valid @RequestBody OptimizationRequestDTO request) {
        return ResponseEntity.ok(optimizerService.optimizeQuery(request.getSqlQuery()));
    }

    @GetMapping("/health-check")
    public ResponseEntity<Map<String, String>> structuralHealth() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "engine", "PostgreSQL Optimization Engine Active",
                "runtime", "Java 21 Virtual Machine Environment"
        ));
    }
}

package com.AiProject.SqlOptimizer.exceptions;

public class InvalidSqlException extends RuntimeException {
    public InvalidSqlException(String message) {
        super(message);
    }
}
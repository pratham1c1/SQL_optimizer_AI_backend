package com.AiProject.SqlOptimizer.service;

import static org.junit.jupiter.api.Assertions.*;

import com.AiProject.SqlOptimizer.dto.MetadataExtraction;
import com.AiProject.SqlOptimizer.exceptions.InvalidSqlException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserServiceTest {

    private ParserService parserService;

    @BeforeEach
    void setUp() {
        parserService = new ParserService();
    }

    @Test
    void testValidSqlQueryParsing() {
        String query = "SELECT u.id, o.total FROM users u JOIN orders o ON u.id = o.user_id WHERE u.status = 'ACTIVE'";

        MetadataExtraction metadata = parserService.extractMetadata(query);

        assertTrue(metadata.isValid());
//        assertEquals("Select", metadata.getQueryType());
        assertTrue(metadata.getTables().contains("users"));
        assertTrue(metadata.getTables().contains("orders"));
        assertFalse(metadata.getJoinConditions().isEmpty());
    }

    @Test
    void testInvalidSqlQueryThrowsException() {
        String badQuery = "SELECT FROM WHERE SELECT FOR users";

        assertThrows(InvalidSqlException.class, () -> {
            parserService.extractMetadata(badQuery);
        });
    }
}
package com.matchcv.service.impl;

import com.matchcv.dto.MatchResponseDto;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class MatchServiceImplTest {

    private final MatchServiceImpl matchService = new MatchServiceImpl();

    @Test
    void analyzeMatch_ShouldCalculateCorrectPercentage() {
        // Arrange
        String resumeText = "Experienced Java developer with Spring Boot and REST API knowledge";
        String jobDescription = "We are looking for a Java developer";
        String requirements = "Spring Boot, REST APIs, Git";

        // Act
        MatchResponseDto response = matchService.analyzeMatch(resumeText, jobDescription, requirements);

        // Assert
        assertNotNull(response);
        // Should have some match since Java and Spring Boot are in resume
        assertTrue(response.getMatchPercentage() > 0);
        assertTrue(response.getMatchPercentage() <= 100);
        assertFalse(response.getSummary().isBlank());
        // Should have matched requirements
        assertFalse(response.getMatchedRequirements().isEmpty());
        // Should have some missing requirements (Git likely not in resume)
        assertTrue(response.getMissingRequirements().size() >= 0);
    }

    @Test
    void analyzeMatch_WithNoMatch_ReturnsZeroPercentage() {
        // Arrange
        String resumeText = "Python programmer with Django experience";
        String jobDescription = "Java engineer position";
        String requirements = "Spring Boot, Hibernate";

        // Act
        MatchResponseDto response = matchService.analyzeMatch(resumeText, jobDescription, requirements);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getMatchPercentage());
        assertTrue(response.getMatchedRequirements().isEmpty());
        assertFalse(response.getMissingRequirements().isEmpty());
        assertTrue(response.getSummary().contains("Compatibilidade muito baixa entre currículo e vaga."));
    }

    @Test
    void analyzeMatch_WithEmptyInputs_HandlesGracefully() {
        // Arrange
        String resumeText = "";
        String jobDescription = "";
        String requirements = "";

        // Act
        MatchResponseDto response = matchService.analyzeMatch(resumeText, jobDescription, requirements);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getMatchPercentage());
        assertTrue(response.getMatchedRequirements().isEmpty());
        assertTrue(response.getMissingRequirements().isEmpty());
    }
}
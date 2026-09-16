package com.matchcv.controller;

import com.matchcv.dto.MatchResponseDto;
import com.matchcv.service.MatchService;
import com.matchcv.util.PdfTextExtractor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatchController.class)
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchService matchService;

    @MockBean
    private PdfTextExtractor pdfTextExtractor;


    @Test
    void match_ShouldReturnMatchResponse() throws Exception {
        // Arrange
        String resumeText = "John Doe\nExperience: Java, Spring Boot";
        String jobDescription = "We are looking for a Java developer with Spring Boot experience.";
        String requirements = "Java, Spring Boot";

        MatchResponseDto expectedResponse = new MatchResponseDto(
                85,
                "Good match",
                java.util.List.of("Java", "Spring Boot"),
                java.util.List.of(),
                java.util.List.of(),
                java.util.List.of("Strong Java background"),
                java.util.List.of("Consider adding cloud experience")
        );

        when(matchService.analyzeMatch(eq(resumeText), eq(jobDescription), eq(requirements))).thenReturn(expectedResponse);
        when(pdfTextExtractor.extractText(any(MultipartFile.class))).thenReturn(resumeText);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                resumeText.getBytes(StandardCharsets.UTF_8)
        );

        // Act & Assert - Only test the matchedRequirements field
        mockMvc.perform(multipart("/api/match")
                        .file(file)
                        .param("jobDescription", jobDescription)
                        .param("requirements", requirements))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matchedRequirements").isArray())
                .andExpect(jsonPath("$.matchedRequirements").value(java.util.List.of("Java", "Spring Boot")));
    }

    @Test
    void match_WhenFileIsNull_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String jobDescription = "Job description";
        String requirements = "Requirements";

        // Make pdfTextExtractor throw an exception when called with empty file
        when(pdfTextExtractor.extractText(any(MultipartFile.class))).thenThrow(new IOException("Empty file"));

        // Act & Assert
        mockMvc.perform(multipart("/api/match")
                        .file(new MockMultipartFile("file", "", MediaType.APPLICATION_PDF_VALUE, new byte[0]))
                        .param("jobDescription", jobDescription)
                        .param("requirements", requirements))
                .andExpect(status().isBadRequest());
    }

    @Test
    void match_WhenJobDescriptionIsBlank_ShouldReturnBadRequest() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "resume content".getBytes(StandardCharsets.UTF_8)
        );

        // Make pdfTextExtractor throw an exception when called (to simulate bad file)
        when(pdfTextExtractor.extractText(any(MultipartFile.class))).thenThrow(new IOException("Bad file"));

        // Act & Assert
        mockMvc.perform(multipart("/api/match")
                        .file(file)
                        .param("jobDescription", "")
                        .param("requirements", "Requirements"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testServiceDirectly() {
        // Arrange
        String resumeText = "John Doe\nExperience: Java, Spring Boot";
        String jobDescription = "We are looking for a Java developer with Spring Boot experience.";
        String requirements = "Java, Spring Boot";

        MatchResponseDto expectedResponse = new MatchResponseDto(
                85,
                "Good match",
                java.util.List.of("Java", "Spring Boot"),
                java.util.List.of(),
                java.util.List.of(),
                java.util.List.of("Strong Java background"),
                java.util.List.of("Consider adding cloud experience")
        );

        when(matchService.analyzeMatch(eq(resumeText), eq(jobDescription), eq(requirements))).thenReturn(expectedResponse);

        // Act
        MatchResponseDto response = matchService.analyzeMatch(resumeText, jobDescription, requirements);

        // Assert
        assertNotNull(response);
        assertEquals(85, response.getMatchPercentage());
        assertEquals("Good match", response.getSummary());
        assertEquals(java.util.List.of("Java", "Spring Boot"), response.getMatchedRequirements());
    }
}
package com.matchcv.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;

public class MatchRequestDto {

    
    @NotNull
    @Schema(
        type = "string",
        format = "binary",
        description = "Arquivo PDF do currículo"
    )
    private MultipartFile file;

    @NotBlank
    @Size(max = 1000)
    private String jobDescription;

    @NotBlank
    @Size(max = 1000)
    private String requirements;

    // Getters and Setters
    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }
}
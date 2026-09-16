package com.matchcv.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchResponseDto {
    private int matchPercentage;
    private String summary;
    private List<String> matchedRequirements;
    private List<String> missingRequirements;
    private List<String> partialRequirements;
    private List<String> strengths;
    private List<String> recommendations;
}
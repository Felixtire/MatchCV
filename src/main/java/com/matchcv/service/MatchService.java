package com.matchcv.service;

import com.matchcv.dto.MatchResponseDto;

public interface MatchService {
    MatchResponseDto analyzeMatch(String resumeText, String jobDescription, String requirements);
}
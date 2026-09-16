package com.matchcv.service.impl;

import com.matchcv.dto.MatchResponseDto;
import com.matchcv.service.MatchService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class MatchServiceImpl implements MatchService {

    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-zA-Z0-9]+");

    @Override
    public MatchResponseDto analyzeMatch(String resumeText, String jobDescription, String requirements) {
        // Combine job description and requirements for matching
        String jobText = jobDescription + " " + requirements;
        
        // Extract words from resume and job text
        Set<String> resumeWords = extractWords(resumeText);
        Set<String> jobWords = extractWords(jobText);

        // Handle edge case: no job words
        if (jobWords.isEmpty()) {
            return new MatchResponseDto(
                    0,
                    "Nenhum requisito fornecido para matching.",
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList()
            );
        }

        // Calculate matched and missing words
        Set<String> matched = new HashSet<>(resumeWords);
        matched.retainAll(jobWords);

        Set<String> missing = new HashSet<>(jobWords);
        missing.removeAll(resumeWords);

        // Calculate match percentage
        int matchPercentage = (int) (((double) matched.size() / jobWords.size()) * 100);

        // Prepare response components
        List<String> matchedList = new ArrayList<>(matched);
        Collections.sort(matchedList);
        List<String> missingList = new ArrayList<>(missing);
        Collections.sort(missingList);

        // For simplicity, we treat all matched as strengths and missing as recommendations
        List<String> strengths = new ArrayList<>(matchedList);
        List<String> recommendations = new ArrayList<>();
        for (String req : missingList) {
            recommendations.add("Considere adicionar experiência com '" + req + "' ao currículo");
        }

        // Create summary based on percentage
        String summary;
        if (matchPercentage >= 80) {
            summary = "Excelente compatibilidade entre currículo e vaga.";
        } else if (matchPercentage >= 60) {
            summary = "Boa compatibilidade entre currículo e vaga.";
        } else if (matchPercentage >= 40) {
            summary = "Compatibilidade moderada entre currículo e vaga.";
        } else if (matchPercentage >= 20) {
            summary = "Baixa compatibilidade entre currículo e vaga.";
        } else {
            summary = "Compatibilidade muito baixa entre currículo e vaga.";
        }

        // Partial requirements and additional recommendations can be added if needed
        List<String> partialRequirements = Collections.emptyList();

        return new MatchResponseDto(
                matchPercentage,
                summary,
                matchedList,
                missingList,
                partialRequirements,
                strengths,
                recommendations
        );
    }

    /**
     * Extracts words from text, converting to lowercase and splitting by non-alphanumeric characters.
     * Filters out empty strings and words with length less than 2.
     */
    private Set<String> extractWords(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptySet();
        }
        String lower = text.toLowerCase(Locale.ROOT);
        String[] words = NON_ALPHANUMERIC.split(lower);
        Set<String> wordSet = new HashSet<>();
        for (String word : words) {
            if (word.length() >= 2) { // Ignore very short words
                wordSet.add(word);
            }
        }
        return wordSet;
    }
}
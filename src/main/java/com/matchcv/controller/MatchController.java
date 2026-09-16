package com.matchcv.controller;

import com.matchcv.dto.MatchRequestDto;
import com.matchcv.dto.MatchResponseDto;
import com.matchcv.service.MatchService;
import com.matchcv.util.PdfTextExtractor;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final MatchService matchService;
    private final PdfTextExtractor pdfTextExtractor;

    @Autowired
    public MatchController(MatchService matchService, PdfTextExtractor pdfTextExtractor) {
        this.matchService = matchService;
        this.pdfTextExtractor = pdfTextExtractor;
    }

  @PostMapping(
        value = "/match",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
)
    public ResponseEntity<MatchResponseDto> match(
        @RequestPart("file") MultipartFile file,
        @RequestPart("jobDescription") String jobDescription,
        @RequestPart("requirements") String requirements
) {
    try {
        String resumeText = pdfTextExtractor.extractText(file);

        MatchResponseDto response = matchService.analyzeMatch(
                resumeText,
                jobDescription,
                requirements
        );

        return ResponseEntity.ok(response);

    } catch (Exception e) {
        return ResponseEntity.badRequest().build();
    }
}
}
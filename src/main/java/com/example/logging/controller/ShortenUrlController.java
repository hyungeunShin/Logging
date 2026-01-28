package com.example.logging.controller;

import com.example.logging.dto.ShortenUrlCreateRequestDto;
import com.example.logging.dto.ShortenUrlCreateResponseDto;
import com.example.logging.dto.ShortenUrlInformationDto;
import com.example.logging.service.ShortenUrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ShortenUrlController {
    private final ShortenUrlService service;

    @GetMapping("/shortenUrls")
    public ResponseEntity<List<ShortenUrlInformationDto>> findAll() {
        log.info("findAll");
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping("/shortenUrls")
    public ResponseEntity<ShortenUrlCreateResponseDto> save(@RequestBody @Validated ShortenUrlCreateRequestDto dto) {
        log.info("save: {}", dto);
        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping("/shortenUrls/{shortenUrlKey}")
    public ResponseEntity<ShortenUrlInformationDto> findShortenUrlByShortenUrlKey(@PathVariable String shortenUrlKey) {
        log.info("findShortenUrlByShortenUrlKey");
        return ResponseEntity.ok(service.findShortenUrlByShortenUrlKey(shortenUrlKey));
    }

    @GetMapping("/{shortenUrlKey}")
    public ResponseEntity<Void> redirect(@PathVariable String shortenUrlKey) throws URISyntaxException {
        log.info("redirect");
        String originalUrl = service.findOriginalUrlByShortenUrlKey(shortenUrlKey);

        URI redirectUri = new URI(originalUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);

        return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
    }
}

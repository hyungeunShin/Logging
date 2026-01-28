package com.example.logging.dto;

import com.example.logging.domain.ShortenUrl;

public record ShortenUrlCreateResponseDto(String originalUrl, String shortenUrlKey) {
    public static ShortenUrlCreateResponseDto from(ShortenUrl shortenUrl) {
        return new ShortenUrlCreateResponseDto(shortenUrl.getOriginalUrl(), shortenUrl.getShortenUrlKey());
    }
}

package com.example.logging.dto;

import com.example.logging.domain.ShortenUrl;

public record ShortenUrlInformationDto(String originalUrl, String shortenUrlKey, Long redirectCount) {
    public static ShortenUrlInformationDto from(ShortenUrl shortenUrl) {
        return new ShortenUrlInformationDto(shortenUrl.getOriginalUrl(), shortenUrl.getShortenUrlKey(), shortenUrl.getRedirectCount());
    }
}
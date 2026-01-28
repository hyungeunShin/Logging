package com.example.logging.service;

import com.example.logging.domain.ShortenUrl;
import com.example.logging.dto.ShortenUrlCreateRequestDto;
import com.example.logging.dto.ShortenUrlCreateResponseDto;
import com.example.logging.dto.ShortenUrlInformationDto;
import com.example.logging.exception.LackOfShortenUrlKeyException;
import com.example.logging.exception.NotFoundShortenUrlException;
import com.example.logging.repository.ShortenUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShortenUrlService {
    private final ShortenUrlRepository repository;

    public List<ShortenUrlInformationDto> findAll() {
        List<ShortenUrl> shortenUrls = repository.findAll();

        return shortenUrls.stream()
                          .map(ShortenUrlInformationDto::from)
                          .toList();
    }

    public ShortenUrlCreateResponseDto save(ShortenUrlCreateRequestDto dto) {
        String originalUrl = dto.originalUrl();
        String shortenUrlKey = getUniqueShortenUrlKey();

        ShortenUrl shortenUrl = new ShortenUrl(originalUrl, shortenUrlKey);
        repository.save(shortenUrl);

        return ShortenUrlCreateResponseDto.from(shortenUrl);
    }

    public ShortenUrlInformationDto findShortenUrlByShortenUrlKey(String shortenUrlKey) {
        ShortenUrl shortenUrl = repository.findShortenUrlByShortenUrlKey(shortenUrlKey);

        if(shortenUrl == null) {
            throw new NotFoundShortenUrlException();
        }

        return ShortenUrlInformationDto.from(shortenUrl);
    }

    public String findOriginalUrlByShortenUrlKey(String shortenUrlKey) {
        ShortenUrl shortenUrl = repository.findShortenUrlByShortenUrlKey(shortenUrlKey);

        if(shortenUrl == null) {
            throw new NotFoundShortenUrlException();
        }

        shortenUrl.increaseRedirectCount();
        repository.save(shortenUrl);

        return shortenUrl.getOriginalUrl();
    }

    private String getUniqueShortenUrlKey() {
        final int MAX_RETRY_COUNT = 5;
        int count = 0;

        while(MAX_RETRY_COUNT > count++) {
            String shortenUrlKey = ShortenUrl.generateShortenUrlKey();
            ShortenUrl shortenUrl = repository.findShortenUrlByShortenUrlKey(shortenUrlKey);

            if(shortenUrl == null) {
                return shortenUrlKey;
            }
        }

        throw new LackOfShortenUrlKeyException();
    }
}

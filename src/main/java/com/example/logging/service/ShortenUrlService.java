package com.example.logging.service;

import com.example.logging.domain.ShortenUrl;
import com.example.logging.dto.ShortenUrlCreateRequestDto;
import com.example.logging.dto.ShortenUrlCreateResponseDto;
import com.example.logging.dto.ShortenUrlInformationDto;
import com.example.logging.exception.LackOfShortenUrlKeyException;
import com.example.logging.exception.NotFoundShortenUrlException;
import com.example.logging.repository.ShortenUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
        log.info("shortenUrl 생성: {}", shortenUrl);

        return ShortenUrlCreateResponseDto.from(shortenUrl);
    }

    public ShortenUrlInformationDto findShortenUrlByShortenUrlKey(String shortenUrlKey) {
        ShortenUrl shortenUrl = repository.findShortenUrlByShortenUrlKey(shortenUrlKey);

        if(shortenUrl == null) {
            throw new NotFoundShortenUrlException("단축 URL을 찾지 못했습니다. shortenUrlKey=" + shortenUrlKey);
        }

        return ShortenUrlInformationDto.from(shortenUrl);
    }

    public String findOriginalUrlByShortenUrlKey(String shortenUrlKey) {
        ShortenUrl shortenUrl = repository.findShortenUrlByShortenUrlKey(shortenUrlKey);

        if(shortenUrl == null) {
            throw new NotFoundShortenUrlException("단축 URL을 찾지 못했습니다. shortenUrlKey=" + shortenUrlKey);
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

            log.warn("단축 URL 생성 재시도 횟수 : {}", count + 1);
        }

        throw new LackOfShortenUrlKeyException();
    }
}

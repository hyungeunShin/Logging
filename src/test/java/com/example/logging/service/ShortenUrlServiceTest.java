package com.example.logging.service;

import com.example.logging.dto.ShortenUrlCreateRequestDto;
import com.example.logging.dto.ShortenUrlCreateResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShortenUrlServiceTest {
    @Autowired
    private ShortenUrlService service;

    @Test
    @DisplayName("URL을 단축한 후 단축된 URL 키로 조회하면 원래 URL이 조회되어야 함")
    void test() {
        String expectedOriginalUrl = "https://www.naver.com";
        ShortenUrlCreateRequestDto request = new ShortenUrlCreateRequestDto(expectedOriginalUrl);

        ShortenUrlCreateResponseDto response = service.save(request);
        String shortenUrlKey = response.shortenUrlKey();

        String originalUrl = service.findOriginalUrlByShortenUrlKey(shortenUrlKey);

        Assertions.assertEquals(expectedOriginalUrl, originalUrl);
    }
}
package com.example.logging.service;

import com.example.logging.domain.ShortenUrl;
import com.example.logging.dto.ShortenUrlCreateRequestDto;
import com.example.logging.exception.LackOfShortenUrlKeyException;
import com.example.logging.repository.ShortenUrlRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShortenUrlServiceUnitTest {
    @Mock
    private ShortenUrlRepository repository;

    @InjectMocks
    private ShortenUrlService service;

    @Test
    @DisplayName("단축 URL이 계속 중복되면 LackOfShortenUrlKeyException 예외가 발생해야 한다")
    void throwLackOfShortenUrlKeyExceptionTest() {
        ShortenUrlCreateRequestDto shortenUrlCreateRequestDto = new ShortenUrlCreateRequestDto(null);

        Mockito.when(repository.findShortenUrlByShortenUrlKey(Mockito.any())).thenReturn(new ShortenUrl(null, null));

        Assertions.assertThrows(LackOfShortenUrlKeyException.class, () -> service.save(shortenUrlCreateRequestDto));
    }
}

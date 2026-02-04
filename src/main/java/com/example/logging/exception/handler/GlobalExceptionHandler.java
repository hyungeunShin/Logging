package com.example.logging.exception.handler;

import com.example.logging.exception.LackOfShortenUrlKeyException;
import com.example.logging.exception.NotFoundShortenUrlException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LackOfShortenUrlKeyException.class)
    public ResponseEntity<String> handleLackOfShortenUrlKeyException(LackOfShortenUrlKeyException ex) {
        log.error("단축 URL 자원이 부족합니다.");
        return new ResponseEntity<>("단축 URL 자원이 부족합니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundShortenUrlException.class)
    public ResponseEntity<String> handleNotFoundShortenUrlException(NotFoundShortenUrlException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity<>("단축 URL을 찾지 못했습니다.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("유효성 검증 실패 => ");
        ex.getBindingResult().getFieldErrors().forEach(error -> errorMessage.append(String.format("'%s': %s\n", error.getField(), error.getDefaultMessage())));

        log.debug("잘못된 요청 => {}", errorMessage);

        return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
    }
}

package com.example.logging.repository;

import com.example.logging.domain.ShortenUrl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ShortenUrlRepository {
    private final Map<String, ShortenUrl> shortenUrls = new ConcurrentHashMap<>();

    public List<ShortenUrl> findAll() {
        return new ArrayList<>(shortenUrls.values());
    }

    public void save(ShortenUrl shortenUrl) {
        shortenUrls.put(shortenUrl.getShortenUrlKey(), shortenUrl);
    }

    public ShortenUrl findShortenUrlByShortenUrlKey(String shortenUrlKey) {
        return shortenUrls.get(shortenUrlKey);
    }
}

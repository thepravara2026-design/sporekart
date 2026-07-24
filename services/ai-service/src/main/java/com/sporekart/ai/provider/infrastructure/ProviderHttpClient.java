package com.sporekart.ai.provider.infrastructure;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class ProviderHttpClient {

    private final RestTemplate restTemplate;

    public ProviderHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getApiKey(Map<String, String> config, String... keys) {
        for (String key : keys) {
            if (config.containsKey(key) && !config.get(key).isEmpty() && !config.get(key).startsWith("your-")) {
                return config.get(key);
            }
        }
        return null;
    }

    public boolean isConfigured(Map<String, String> config) {
        return config != null && !config.isEmpty()
                && !config.getOrDefault("endpoint", "").contains("your-");
    }

    public String postToProvider(String url, Map<String, Object> body, Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.forEach(httpHeaders::set);
        }
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}

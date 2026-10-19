package com.diabetesapp.service;

import com.diabetesapp.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RestTemplate restTemplate;

    @Value("${python.ai.service.url:http://localhost:8000}")
    private String pythonServiceUrl;

    /**
     *Forwards chat messages to the Python AI service and returns the response.
     *Falls back to a safe default if the Python service is unavailable.
     */
    public UserDto.ChatResponse chat(String message) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = Map.of("message", message);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    pythonServiceUrl + "/chat", request, Map.class);

            UserDto.ChatResponse chatResponse = new UserDto.ChatResponse();
            chatResponse.setReply(
                    response.getBody() != null
                            ? (String) response.getBody().get("reply")
                            : fallbackReply());
            return chatResponse;

        } catch (Exception e) {
            UserDto.ChatResponse fallback = new UserDto.ChatResponse();
            fallback.setReply(fallbackReply());
            return fallback;
        }
    }

    private String fallbackReply() {
        return "I'm your diabetes care assistant. While I can provide general information, "
                + "please consult your healthcare provider for personalised medical advice.";
    }
}

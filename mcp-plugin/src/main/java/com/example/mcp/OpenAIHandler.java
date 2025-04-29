package com.example.mcp;

import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import org.bukkit.ChatColor;

import java.time.Duration;

public class OpenAIHandler {
    private final OpenAiService service;
    private static final String MODEL = "gpt-3.5-turbo-instruct";
    private static final int MAX_TOKENS = 150;
    private static final double TEMPERATURE = 0.7;

    public OpenAIHandler(String apiKey) {
        this.service = new OpenAiService(apiKey, Duration.ofSeconds(30));
    }

    public String generateResponse(String prompt) {
        try {
            CompletionRequest request = CompletionRequest.builder()
                    .model(MODEL)
                    .prompt(prompt)
                    .maxTokens(MAX_TOKENS)
                    .temperature(TEMPERATURE)
                    .build();

            CompletionResult result = service.createCompletion(request);
            return result.getChoices().get(0).getText();
        } catch (Exception e) {
            return ChatColor.RED + "Error generating response: " + e.getMessage();
        }
    }
} 
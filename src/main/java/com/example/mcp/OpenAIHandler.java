package com.example.mcp;

import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.time.Duration;

public class OpenAIHandler {
    private final OpenAiService service;
    private final FileConfiguration config;
    private static final String MODEL = "gpt-3.5-turbo-instruct";

    public OpenAIHandler(String apiKey, FileConfiguration config) {
        this.config = config;
        int timeout = config.getInt("settings.timeout", 30);
        this.service = new OpenAiService(apiKey, Duration.ofSeconds(timeout));
    }

    public String generateResponse(String prompt) {
        try {
            int maxTokens = config.getInt("settings.max-tokens", 150);
            double temperature = config.getDouble("settings.temperature", 0.7);

            CompletionRequest request = CompletionRequest.builder()
                    .model(MODEL)
                    .prompt(prompt)
                    .maxTokens(maxTokens)
                    .temperature(temperature)
                    .build();

            CompletionResult result = service.createCompletion(request);
            return result.getChoices().get(0).getText();
        } catch (Exception e) {
            return ChatColor.RED + "Error generating response: " + e.getMessage();
        }
    }
} 
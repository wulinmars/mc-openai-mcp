package com.example.mcp;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.time.Duration;
import java.net.InetSocketAddress;
import java.net.Proxy;
import okhttp3.OkHttpClient;
import java.util.ArrayList;
import java.util.List;

public class OpenAIHandler {
    private final OpenAiService service;
    private final FileConfiguration config;
    private static final String MODEL = "gpt-3.5-turbo";

    public OpenAIHandler(String apiKey, FileConfiguration config) {
        this.config = config;
        int timeout = config.getInt("settings.timeout", 30);
        
        // 如果配置了代理，设置系统代理
        if (config.getBoolean("proxy.enabled", false)) {
            String proxyHost = config.getString("proxy.host", "127.0.0.1");
            int proxyPort = config.getInt("proxy.port", 7890);
            System.setProperty("http.proxyHost", proxyHost);
            System.setProperty("http.proxyPort", String.valueOf(proxyPort));
            System.setProperty("https.proxyHost", proxyHost);
            System.setProperty("https.proxyPort", String.valueOf(proxyPort));
        }

        this.service = new OpenAiService(apiKey, Duration.ofSeconds(timeout));
    }

    public String generateResponse(String prompt) {
        try {
            List<ChatMessage> messages = new ArrayList<>();
            messages.add(new ChatMessage("user", prompt));

            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(MODEL)
                    .messages(messages)
                    .maxTokens(config.getInt("settings.max-tokens", 150))
                    .temperature(config.getDouble("settings.temperature", 0.7))
                    .build();

            return service.createChatCompletion(request).getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("exceeded your current quota")) {
                return ChatColor.RED + "OpenAI API 配额已超限。请联系服务器管理员检查 API 密钥的配额和账单状态。";
            } else if (errorMessage != null && errorMessage.contains("Rate limit")) {
                return ChatColor.RED + "API 请求过于频繁，请稍后再试。";
            } else {
                return ChatColor.RED + "生成回复时出错: " + e.getMessage();
            }
        }
    }
} 
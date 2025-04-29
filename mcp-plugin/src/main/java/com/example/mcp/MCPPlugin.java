package com.example.mcp;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MCPPlugin extends JavaPlugin {
    private OpenAIHandler openAIHandler;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        // 保存默认配置文件
        saveDefaultConfig();
        config = getConfig();

        // 初始化 OpenAI 处理器
        String apiKey = config.getString("openai.api-key");
        if (apiKey == null || apiKey.isEmpty()) {
            getLogger().severe("OpenAI API key is not set in config.yml!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        openAIHandler = new OpenAIHandler(apiKey);

        // 注册命令
        getCommand("ai").setExecutor(new MCPCommand(this, openAIHandler));

        getLogger().info("MCP plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MCP plugin has been disabled!");
    }

    public FileConfiguration getPluginConfig() {
        return config;
    }
} 
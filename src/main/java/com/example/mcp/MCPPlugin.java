package com.example.mcp;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MCPPlugin extends JavaPlugin {
    private static MCPPlugin instance;
    private OpenAIHandler openAIHandler;
    private FileConfiguration config;
    private File configFile;

    @Override
    public void onEnable() {
        instance = this;
        
        // Create plugin data folder if it doesn't exist
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        // Load or create config file
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        // Initialize OpenAI handler with API key from config
        String apiKey = config.getString("openai.api-key");
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your-api-key-here")) {
            getLogger().severe("OpenAI API key not found in config.yml! Please set your API key in plugins/MCP/config.yml");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        openAIHandler = new OpenAIHandler(apiKey, config);
        
        // Register command
        getCommand("mcp").setExecutor(new MCPCommand(this));
        
        getLogger().info("MCP plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MCP plugin has been disabled!");
    }

    public OpenAIHandler getOpenAIHandler() {
        return openAIHandler;
    }

    public static MCPPlugin getInstance() {
        return instance;
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        // Reinitialize OpenAI handler with new config
        String apiKey = config.getString("openai.api-key");
        if (apiKey != null && !apiKey.isEmpty() && !apiKey.equals("your-api-key-here")) {
            openAIHandler = new OpenAIHandler(apiKey, config);
        }
    }
} 
package com.example.mcp;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class MCPPlugin extends JavaPlugin {
    private FileConfiguration config;
    private OpenAIHandler openAIHandler;
    private static MCPPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        
        // 保存默认配置文件
        saveDefaultConfig();
        
        // 初始化 OpenAI 处理器
        String apiKey = getConfig().getString("openai.api-key");
        if (apiKey == null || apiKey.isEmpty()) {
            getLogger().severe("OpenAI API key not found in config.yml!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        openAIHandler = new OpenAIHandler(apiKey);
        
        // 注册命令
        getCommand("mcp").setExecutor(new MCPCommand(openAIHandler));
        
        getLogger().info("MCP Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MCP Plugin has been disabled!");
    }

    public OpenAIHandler getOpenAIHandler() {
        return openAIHandler;
    }

    public static MCPPlugin getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ai")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be used by players!");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage("Usage: /ai <message>");
                return true;
            }

            Player player = (Player) sender;
            String message = String.join(" ", args);

            // 异步处理AI请求
            getServer().getScheduler().runTaskAsynchronously(this, () -> {
                try {
                    String response = openAIHandler.getAIResponse(message);
                    player.sendMessage("AI: " + response);
                } catch (Exception e) {
                    player.sendMessage("Error: " + e.getMessage());
                    getLogger().severe("Error processing AI request: " + e.getMessage());
                }
            });

            return true;
        }
        return false;
    }
} 
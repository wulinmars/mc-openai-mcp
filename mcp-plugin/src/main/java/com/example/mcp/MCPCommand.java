package com.example.mcp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class MCPCommand implements CommandExecutor {
    private final MCPPlugin plugin;
    private final OpenAIHandler openAIHandler;

    public MCPCommand(MCPPlugin plugin, OpenAIHandler openAIHandler) {
        this.plugin = plugin;
        this.openAIHandler = openAIHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /mcp <message>");
            return true;
        }

        Player player = (Player) sender;
        String message = String.join(" ", args);

        // 异步处理AI响应
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            String response = openAIHandler.generateResponse(message);
            // 在主线程中发送消息
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                player.sendMessage(ChatColor.GREEN + "AI: " + ChatColor.WHITE + response);
            });
        });

        return true;
    }
} 
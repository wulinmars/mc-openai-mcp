package com.example.mcp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MCPCommand implements CommandExecutor {
    private final OpenAIHandler openAIHandler;

    public MCPCommand(OpenAIHandler openAIHandler) {
        this.openAIHandler = openAIHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /mcp <prompt>");
            return true;
        }

        Player player = (Player) sender;
        String prompt = String.join(" ", args);
        
        // Send initial message
        player.sendMessage(ChatColor.GREEN + "Generating response...");
        
        // Generate response asynchronously
        MCPPlugin.getInstance().getServer().getScheduler().runTaskAsynchronously(MCPPlugin.getInstance(), () -> {
            String response = openAIHandler.generateResponse(prompt);
            // Send response on main thread
            MCPPlugin.getInstance().getServer().getScheduler().runTask(MCPPlugin.getInstance(), () -> {
                player.sendMessage(ChatColor.WHITE + response);
            });
        });

        return true;
    }
} 
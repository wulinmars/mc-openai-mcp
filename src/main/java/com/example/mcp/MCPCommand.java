package com.example.mcp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MCPCommand implements CommandExecutor {
    private final MCPPlugin plugin;

    public MCPCommand(MCPPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;
        
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /ai <prompt>");
            return true;
        }

        String prompt = String.join(" ", args);
        String response = plugin.getOpenAIHandler().generateResponse(prompt);
        player.sendMessage(ChatColor.GREEN + "AI: " + response);
        
        return true;
    }
} 
package fitox.filter.chatFilter.commands;

import fitox.filter.chatFilter.ChatFilter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatFilterCommand implements CommandExecutor, TabCompleter {
    
    private final ChatFilter plugin;
    private boolean filterEnabled = true;
    
    public ChatFilterCommand(ChatFilter plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("chatfilter.admin")) {
            sender.sendMessage(Component.text("You cant run this cmd")
                    .color(NamedTextColor.RED));
            return true;
        }
        
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        String arg = args[0].toLowerCase();
        
        // Enable/Disable commands
        if (arg.equals("true") || arg.equals("on") || arg.equals("enable")) {
            filterEnabled = true;
            sender.sendMessage(Component.text("✓ Chat Filter Enabled")
                    .color(NamedTextColor.GREEN));
            return true;
        }
        
        if (arg.equals("false") || arg.equals("off") || arg.equals("disable")) {
            filterEnabled = false;
            sender.sendMessage(Component.text("✓ Chat Filter Disabled")
                    .color(NamedTextColor.RED));
            sender.sendMessage(Component.text("Warning: Messages wont flag.")
                    .color(NamedTextColor.YELLOW));
            return true;
        }
        
        // Reload command
        if (arg.equals("reload") || arg.equals("refresh")) {
            sender.sendMessage(Component.text("Reloading config files")
                    .color(NamedTextColor.YELLOW));
            
            try {
                plugin.getFilterConfig().forceRecreateConfigs();
                plugin.reloadDetector();
                sender.sendMessage(Component.text("✓ Config files reloaded!")
                        .color(NamedTextColor.GREEN));
                sender.sendMessage(Component.text("All word lists have been updated")
                        .color(NamedTextColor.GRAY));
            } catch (Exception e) {
                sender.sendMessage(Component.text("✗ Error reloading configs: " + e.getMessage())
                        .color(NamedTextColor.RED));
                e.printStackTrace();
            }
            return true;
        }
        
        sendHelp(sender);
        return true;
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Component.text("=== Chat Filter Commands ===")
                .color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("Status: ")
                .color(NamedTextColor.GRAY)
                .append(Component.text(filterEnabled ? "Enabled" : "Disabled")
                        .color(filterEnabled ? NamedTextColor.GREEN : NamedTextColor.RED)));
        sender.sendMessage(Component.text("/cf <true|false> - Enable/disable filter")
                .color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text("/cf reload - Reload config files")
                .color(NamedTextColor.GRAY));
    }
    
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("true", "false", "reload");
        }
        
        return new ArrayList<>();
    }

    public boolean isFilterEnabled() {
        return filterEnabled;
    }
}

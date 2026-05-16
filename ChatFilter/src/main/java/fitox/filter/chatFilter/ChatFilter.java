package fitox.filter.chatFilter;

import fitox.filter.chatFilter.commands.ChatFilterCommand;
import fitox.filter.chatFilter.config.FilterConfig;
import fitox.filter.chatFilter.listener.ChatListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChatFilter extends JavaPlugin {

    private FilterConfig filterConfig;
    private ChatListener chatListener;
    private ChatFilterCommand commandExecutor;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        
        filterConfig = new FilterConfig(this);
        filterConfig.loadConfig();

        chatListener = new ChatListener(this, filterConfig);
        Bukkit.getPluginManager().registerEvents(chatListener, this);

        commandExecutor = new ChatFilterCommand(this);
        PluginCommand command = getCommand("chatfilter");
        if (command != null) {
            command.setExecutor(commandExecutor);
            command.setTabCompleter(commandExecutor);
        }

    }

    @Override
    public void onDisable() {
        getLogger().info("ChatFilter disabled!");
    }

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public ChatListener getChatListener() {
        return chatListener;
    }

    public ChatFilterCommand getCommandExecutor() {
        return commandExecutor;
    }

    public void reloadDetector() {
        filterConfig.loadConfig();
        chatListener = new ChatListener(this, filterConfig);
    }
}

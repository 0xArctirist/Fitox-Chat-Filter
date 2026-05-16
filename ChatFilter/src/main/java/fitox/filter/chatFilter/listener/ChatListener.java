package fitox.filter.chatFilter.listener;

import fitox.filter.chatFilter.ChatFilter;
import fitox.filter.chatFilter.alert.StaffAlertManager;
import fitox.filter.chatFilter.config.FilterConfig;
import fitox.filter.chatFilter.detection.DetectionResult;
import fitox.filter.chatFilter.detection.MultiLayerDetector;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {
    
    private final ChatFilter plugin;
    private final MultiLayerDetector detector;
    private final StaffAlertManager staffAlertManager;
    private final FilterConfig config;
    
    public ChatListener(ChatFilter plugin, FilterConfig config) {
        this.plugin = plugin;
        this.config = config;
        this.detector = new MultiLayerDetector(config);
        this.staffAlertManager = new StaffAlertManager(plugin);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getCommandExecutor().isFilterEnabled()) {
            return;
        }

        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        DetectionResult result = detector.analyze(message, player.getName(), player.getUniqueId());

        if (result.isBlocked()) {
            event.setCancelled(true);
            player.sendMessage(Component.text("You cannot say that.")
                    .color(NamedTextColor.RED));
            staffAlertManager.alertStaff(player, result);
        }
    }

    public DetectionResult testMessage(String message) {
        return detector.analyze(message, "TestUser", java.util.UUID.randomUUID());
    }
    

    public MultiLayerDetector getDetector() {
        return detector;
    }
}

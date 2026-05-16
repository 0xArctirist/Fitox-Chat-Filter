package fitox.filter.chatFilter.alert;

import fitox.filter.chatFilter.detection.DetectionResult;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class StaffAlertManager {
    
    private final Plugin plugin;
    private String webhookUrl;
    private String botUsername;
    private String avatarUrl;

    public StaffAlertManager(Plugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }
    
    public void loadConfig() {
        this.webhookUrl = plugin.getConfig().getString("discord-webhook.url", "");
        this.botUsername = plugin.getConfig().getString("discord-webhook.username", "Chat Filter Alert");
        this.avatarUrl = plugin.getConfig().getString("discord-webhook.avatar-url", "");
    }

    public void alertStaff(Player violator, DetectionResult result) {
        if (!webhookUrl.isEmpty() && !webhookUrl.contains("Webhook")) {
            sendDiscordAlert(violator, result);
        }

        Bukkit.getLogger().warning("[CHAT FILTER] " + violator.getName() + 
                " (" + violator.getUniqueId() + ") - " + result.getOriginalMessage());
    }
    
    private void sendDiscordAlert(Player violator, DetectionResult result) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URL url = new URL(webhookUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("User-Agent", "ChatFilter-Bot");
                connection.setDoOutput(true);

                String json = buildDiscordEmbed(violator, result);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = json.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode != 204 && responseCode != 200) {
                    plugin.getLogger().warning("webhook failed with response code: " + responseCode);
                }

                connection.disconnect();
            } catch (Exception e) {
                plugin.getLogger().severe("failed to send discord alert: " + e.getMessage());
            }
        });
    }
    
    private String buildDiscordEmbed(Player violator, DetectionResult result) {
        int color = 0x8D1927;

        String playerHeadUrl = "https://render.crafty.gg/3d/bust/" + violator.getName();

        String playerUUID = violator.getUniqueId().toString();

        String originalMsg = escapeJson(result.getOriginalMessage());

        return String.format("""
            {
              "username": "%s",
              "avatar_url": "%s",
              "embeds": [{
                "title": "In-game Chat Flag",
                "color": %d,
                "thumbnail": {
                  "url": "%s"
                },
                "fields": [
                  {
                    "name": "IGN",
                    "value": "**%s**",
                    "inline": true
                  },
                  {
                    "name": "UUID",
                    "value": "`%s`",
                    "inline": false
                  },
                  {
                    "name": "Message",
                    "value": "```%s```",
                    "inline": false
                  }
                ],
                "timestamp": "%s",
                "footer": {
                  "text": "Fitox CF"
                }
              }]
            }
            """,
            botUsername,
            avatarUrl,
            color,
            playerHeadUrl,
            violator.getName(),
            playerUUID,
            originalMsg,
            Instant.now().toString()
        );
    }
    

    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}

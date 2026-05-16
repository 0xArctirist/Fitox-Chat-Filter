package fitox.filter.chatFilter.detection;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class ReputationManager {
    
    private final Map<UUID, PlayerReputation> reputations;

    private static final int NEW_PLAYER_THRESHOLD = 50;      // below 50 = new player
    private static final int TRUSTED_PLAYER_THRESHOLD = 200; // above 200 = trusted
    private static final int MAX_REPUTATION = 500;
    private static final int MIN_REPUTATION = 0;

    private static final int CLEAN_MESSAGE_BONUS = 1;
    private static final int VIOLATION_PENALTY = 20;
    private static final int SEVERE_VIOLATION_PENALTY = 50;
    
    public ReputationManager() {
        this.reputations = new ConcurrentHashMap<>();
    }

    public int getReputation(UUID playerId) {
        PlayerReputation rep = reputations.get(playerId);
        return (rep != null) ? rep.getScore() : NEW_PLAYER_THRESHOLD;
    }

    public boolean isNewPlayer(UUID playerId) {
        return getReputation(playerId) < NEW_PLAYER_THRESHOLD;
    }

    public boolean isTrustedPlayer(UUID playerId) {
        return getReputation(playerId) >= TRUSTED_PLAYER_THRESHOLD;
    }

    public String getReputationTier(UUID playerId) {
        int rep = getReputation(playerId);
        
        if (rep < NEW_PLAYER_THRESHOLD) {
            return "New";
        } else if (rep >= TRUSTED_PLAYER_THRESHOLD) {
            return "Trusted";
        } else {
            return "Regular";
        }
    }

    public void rewardCleanMessage(UUID playerId) {
        PlayerReputation rep = reputations.computeIfAbsent(
            playerId,
            k -> new PlayerReputation()
        );
        
        rep.addScore(CLEAN_MESSAGE_BONUS);
    }

    public void penalizeViolation(UUID playerId, boolean severe) {
        PlayerReputation rep = reputations.computeIfAbsent(
            playerId,
            k -> new PlayerReputation()
        );
        
        int penalty = severe ? SEVERE_VIOLATION_PENALTY : VIOLATION_PENALTY;
        rep.subtractScore(penalty);
    }

    // changes score based off actions
    public double getScoreModifier(UUID playerId) {
        if (isNewPlayer(playerId)) {
            return 1.3; // 30% stricter for new players
        } else if (isTrustedPlayer(playerId)) {
            return 0.7; // 30% more lenient for trusted players
        } else {
            return 1.0; // normal for regular players
        }
    }

    public void reset(UUID playerId) {
        reputations.remove(playerId);
    }

    public void clearAll() {
        reputations.clear();
    }

    private static class PlayerReputation {
        private int score;
        private long lastUpdate;
        
        public PlayerReputation() {
            this.score = NEW_PLAYER_THRESHOLD;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public int getScore() {
            return score;
        }
        
        public void addScore(int amount) {
            score = Math.min(score + amount, MAX_REPUTATION);
            lastUpdate = System.currentTimeMillis();
        }
        
        public void subtractScore(int amount) {
            score = Math.max(score - amount, MIN_REPUTATION);
            lastUpdate = System.currentTimeMillis();
        }
        
        public long getLastUpdate() {
            return lastUpdate;
        }
    }
}

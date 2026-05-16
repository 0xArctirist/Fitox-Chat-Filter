package fitox.filter.chatFilter.detection;

import java.util.ArrayList;
import java.util.List;

public class DetectionResult {
    private final String originalMessage;
    private final String normalizedMessage;
    private final List<ViolationType> violations;
    private final int totalScore;
    private final boolean blocked;
    private final String reason;
    
    private DetectionResult(Builder builder) {
        this.originalMessage = builder.originalMessage;
        this.normalizedMessage = builder.normalizedMessage;
        this.violations = builder.violations;
        this.totalScore = builder.totalScore;
        this.blocked = builder.blocked;
        this.reason = builder.reason;
    }
    
    public String getOriginalMessage() {
        return originalMessage;
    }
    
    public String getNormalizedMessage() {
        return normalizedMessage;
    }
    
    public List<ViolationType> getViolations() {
        return violations;
    }
    
    public int getTotalScore() {
        return totalScore;
    }
    
    public boolean isBlocked() {
        return blocked;
    }
    
    public String getReason() {
        return reason;
    }
    
    public static class Builder {
        private String originalMessage;
        private String normalizedMessage;
        private List<ViolationType> violations = new ArrayList<>();
        private int totalScore = 0;
        private boolean blocked = false;
        private String reason = "";
        
        public Builder originalMessage(String originalMessage) {
            this.originalMessage = originalMessage;
            return this;
        }
        
        public Builder normalizedMessage(String normalizedMessage) {
            this.normalizedMessage = normalizedMessage;
            return this;
        }
        
        public Builder addViolation(ViolationType violation) {
            this.violations.add(violation);
            return this;
        }
        
        public Builder totalScore(int totalScore) {
            this.totalScore = totalScore;
            return this;
        }
        
        public Builder blocked(boolean blocked) {
            this.blocked = blocked;
            return this;
        }
        
        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }
        
        public DetectionResult build() {
            return new DetectionResult(this);
        }
    }
}

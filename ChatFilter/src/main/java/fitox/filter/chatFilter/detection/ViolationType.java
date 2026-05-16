package fitox.filter.chatFilter.detection;


public enum ViolationType {
    MILD_INSULT(1, "Mild Insult"),
    PROFANITY_SPAM(2, "Profanity/Spam"),
    SLUR(3, "Slur"),
    DEATH_THREAT(4, "Death Threat"),
    EXTREME_HATE(5, "Extreme Hate/Severe Threat"),
    ADVERTISING(2, "Advertising"),
    SEXUAL_CONTENT(3, "Sexual Content"),
    SPAM(1, "Spam");
    
    private final int severity;
    private final String displayName;
    
    ViolationType(int severity, String displayName) {
        this.severity = severity;
        this.displayName = displayName;
    }
    
    public int getSeverity() {
        return severity;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

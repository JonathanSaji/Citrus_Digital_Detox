import java.time.LocalDateTime;
import java.time.LocalTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// Represents a website or app block rule
public class Block implements Serializable {
    private String targetName; // e.g., "YouTube"
    private LockType lockType;   // "Timer", "Time Range", "Random Text", "Delay", "Bedtime"
    private boolean active;
    private static final long serialVersionUID = 1L;

    private int timesTriggered = 0;

    // Timer
    private LocalDateTime unlockAt;  // block ends at this exact moment

    // Time Range
    private LocalTime rangeStart;
    private LocalTime rangeEnd;
    private Set<java.time.DayOfWeek> activeDays;

    // Random Text
    private int challengeLength;  // number of chars/words to retype

    // Delay
    private int delaySeconds;  // cooldown before app opens

    // Bedtime
    private LocalTime bedStart;
    private LocalTime bedEnd;

    // Emergency
    private String emergencyPassword;

    public Block(String targetName, LockType lockType) {
        this.targetName = targetName;
        this.lockType = lockType;
        this.active = true;
    }



    //Getters/Setters
    public int getTimesTriggered() { return timesTriggered; }
    public void incrementTriggerCount() { timesTriggered++; }

    public String getTargetName() { return targetName; }
    public LockType getLockType() { return lockType; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getUnlockAt() { return unlockAt; }
    public void setUnlockAt(LocalDateTime unlockAt) { this.unlockAt = unlockAt; }

    public LocalTime getRangeStart() { return rangeStart; }
    public void setRangeStart(LocalTime rangeStart) { this.rangeStart = rangeStart; }
    public LocalTime getRangeEnd() { return rangeEnd; }
    public void setRangeEnd(LocalTime rangeEnd) { this.rangeEnd = rangeEnd; }
    public Set<java.time.DayOfWeek> getActiveDays() { return activeDays; }
    public void setActiveDays(Set<java.time.DayOfWeek> activeDays) { this.activeDays = activeDays; }

    public int getChallengeLength() { return challengeLength; }
    public void setChallengeLength(int challengeLength) { this.challengeLength = challengeLength; }

    public int getDelaySeconds() { return delaySeconds; }
    public void setDelaySeconds(int delaySeconds) { this.delaySeconds = delaySeconds; }

    public LocalTime getBedStart() { return bedStart; }
    public void setBedStart(LocalTime bedStart) { this.bedStart = bedStart; }
    public LocalTime getBedEnd() { return bedEnd; }
    public void setBedEnd(LocalTime bedEnd) { this.bedEnd = bedEnd; }

    public String getEmergencyPassword() { return emergencyPassword; }
    public void setEmergencyPassword(String emergencyPassword) { this.emergencyPassword = emergencyPassword; }

    /**
     * Core logic: is this block currently in effect right now?
     * This is the single method the rest of the app will call.
     */
    public boolean isCurrentlyBlocking() {
        if (!active) return false;

        switch (lockType) {
            case TIMER:
                return unlockAt != null && LocalDateTime.now().isBefore(unlockAt);

            case TIME_RANGE:
                LocalTime now = LocalTime.now();
                boolean dayOk = activeDays == null || activeDays.contains(LocalDateTime.now().getDayOfWeek());
                if (!dayOk) return false;
                if (rangeStart.isBefore(rangeEnd)) {
                    return !now.isBefore(rangeStart) && now.isBefore(rangeEnd);
                } else {
                    // range wraps past midnight, e.g. 22:00 - 06:00
                    return !now.isBefore(rangeStart) || now.isBefore(rangeEnd);
                }

            case BEDTIME:
                LocalTime t = LocalTime.now();
                if (bedStart.isBefore(bedEnd)) {
                    return !t.isBefore(bedStart) && t.isBefore(bedEnd);
                } else {
                    return !t.isBefore(bedStart) || t.isBefore(bedEnd);
                }

            case RANDOM_TEXT:
            case DELAY:
            case EMERGENCY:
                // These are "on demand" locks - they stay active until the
                // user completes the challenge/delay/password, handled by
                // the overlay itself, not a time check.
                return true;

            default:
                return false;
        }
    }
    public boolean validateRandomTextInput(String input) {
        if (input == null) return false;
        String[] words = input.trim().split("\\s+");
        if (words.length != challengeLength) return false;

        Set<String> seenWords = new HashSet<>();
        for (String word : words) {
            if (word.length() < 2) return false;
            if (!seenWords.add(word.toLowerCase())) return false; // duplicate found
        }
        return true;
    }
}
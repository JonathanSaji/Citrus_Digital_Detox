import java.time.LocalDateTime;

public class Pass {
    private String targetName;
    private LocalDateTime expiresAt;

    public Pass(String targetName, int minutes) {
        this.targetName = targetName;
        this.expiresAt = LocalDateTime.now().plusMinutes(minutes);
    }

    public String getTargetName() { return targetName; }

    public boolean isActive() {
        return LocalDateTime.now().isBefore(expiresAt);
    }
}
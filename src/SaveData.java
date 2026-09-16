import java.io.Serializable;
import java.util.List;

public class SaveData implements Serializable {
    private List<Block> blocks;
    private int coins;
    private double totalProductiveSeconds;
    private double lifetimeProductiveSeconds;
    private boolean hasLifetimeProductiveSeconds;
    private int secondsPerCoin;
    private static final long serialVersionUID = 1L;

    public SaveData(List<Block> blocks, int coins, double totalProductiveSeconds,
                    double lifetimeProductiveSeconds, int secondsPerCoin) {
        this.blocks = blocks;
        this.coins = coins;
        this.totalProductiveSeconds = totalProductiveSeconds;
        this.lifetimeProductiveSeconds = lifetimeProductiveSeconds;
        this.hasLifetimeProductiveSeconds = true;
        this.secondsPerCoin = secondsPerCoin;
    }
    public int getSecondsPerCoin() { return secondsPerCoin; }

    public List<Block> getBlocks() { return blocks; }
    public int getCoins() { return coins; }
    public double getTotalProductiveSeconds() { return totalProductiveSeconds; }
    public boolean hasLifetimeProductiveSeconds() { return hasLifetimeProductiveSeconds; }
    public double getLifetimeProductiveSeconds() { return lifetimeProductiveSeconds; }
}

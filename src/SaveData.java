import java.io.Serializable;
import java.util.List;

public class SaveData implements Serializable {
    private List<Block> blocks;
    private int coins;
    private double totalProductiveSeconds;

    public SaveData(List<Block> blocks, int coins, double totalProductiveSeconds) {
        this.blocks = blocks;
        this.coins = coins;
        this.totalProductiveSeconds = totalProductiveSeconds;
    }

    public List<Block> getBlocks() { return blocks; }
    public int getCoins() { return coins; }
    public double getTotalProductiveSeconds() { return totalProductiveSeconds; }
}
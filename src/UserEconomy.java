// Manages points, productive tracking, and exchange rates
public class UserEconomy {
    private int coins;
    private double totalProductiveSeconds;
    private static final int SECONDS_PER_COIN = 360; // 6 minutes

    public UserEconomy() {
        this.coins = 0;
        this.totalProductiveSeconds = 0;
    }

    public void addProductiveTime(double seconds) {
        this.totalProductiveSeconds += seconds;
        if (this.totalProductiveSeconds >= SECONDS_PER_COIN) {
            int earnedCoins = (int) (this.totalProductiveSeconds / SECONDS_PER_COIN);
            this.coins += earnedCoins;
            this.totalProductiveSeconds %= SECONDS_PER_COIN; // Keep remainder
        }
    }

    public boolean spendCoins(int amount) {
        if (coins >= amount) {
            coins -= amount;
            return true;
        }
        return false;
    }

    public int getCoins() { return coins; }
    public double getRemainingSecondsToNextCoin() { return SECONDS_PER_COIN - totalProductiveSeconds; }
}
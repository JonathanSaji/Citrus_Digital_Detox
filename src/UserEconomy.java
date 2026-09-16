// Manages points, productive tracking, and exchange rates
public class UserEconomy {
    private int coins;
    private double totalProductiveSeconds;
    private int secondsPerCoin = 360; // 6 minutes, now configurable
    public UserEconomy() {
        this.coins = 0;
        this.totalProductiveSeconds = 0;
    }

    public void addProductiveTime(double seconds) {
        this.totalProductiveSeconds += seconds;
        if (this.totalProductiveSeconds >= secondsPerCoin) {
            int earnedCoins = (int) (this.totalProductiveSeconds / secondsPerCoin);
            this.coins += earnedCoins;
            this.totalProductiveSeconds %= secondsPerCoin;
        }
    }

    public boolean spendCoins(int amount) {
        if (coins >= amount) {
            coins -= amount;
            return true;
        }
        return false;
    }
    public double getTotalProductiveMinutes() {
        return (coins * 6.0) + (totalProductiveSeconds / 60.0);
    }
    public void setCoins(int coins) { this.coins = coins; }
    public double getTotalProductiveSecondsRaw() { return totalProductiveSeconds; }
    public void setTotalProductiveSecondsRaw(double seconds) { this.totalProductiveSeconds = seconds; }

    public int getSecondsPerCoin() { return secondsPerCoin; }
    public void setSecondsPerCoin(int secondsPerCoin) { this.secondsPerCoin = secondsPerCoin; }

    public int getCoins() { return coins; }
    public double getRemainingSecondsToNextCoin() { return secondsPerCoin - totalProductiveSeconds; }
}
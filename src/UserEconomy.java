// Manages points, productive tracking, and exchange rates
public class UserEconomy {
    private int coins;
    // Time not yet converted to a coin.
    private double totalProductiveSeconds;
    // Lifetime time is kept separately so buying coins does not erase history.
    private double lifetimeProductiveSeconds;
    private int secondsPerCoin = 360; // 6 minutes, now configurable
    public UserEconomy() {
        this.coins = 0;
        this.totalProductiveSeconds = 0;
        this.lifetimeProductiveSeconds = 0;
    }

    public synchronized void addProductiveTime(double seconds) {
        if (seconds <= 0) return;
        this.totalProductiveSeconds += seconds;
        this.lifetimeProductiveSeconds += seconds;
        if (this.totalProductiveSeconds >= secondsPerCoin) {
            int earnedCoins = (int) (this.totalProductiveSeconds / secondsPerCoin);
            this.coins += earnedCoins;
            this.totalProductiveSeconds %= secondsPerCoin;
        }
    }

    public synchronized boolean spendCoins(int amount) {
        if (amount < 0) return false;
        if (coins >= amount) {
            coins -= amount;
            return true;
        }
        return false;
    }
    public synchronized double getTotalProductiveMinutes() {
        return lifetimeProductiveSeconds / 60.0;
    }
    public synchronized void setCoins(int coins) { this.coins = Math.max(0, coins); }
    public synchronized double getTotalProductiveSecondsRaw() { return totalProductiveSeconds; }
    public synchronized void setTotalProductiveSecondsRaw(double seconds) {
        this.totalProductiveSeconds = Math.max(0, seconds);
    }
    public synchronized double getLifetimeProductiveSeconds() { return lifetimeProductiveSeconds; }
    public synchronized void setLifetimeProductiveSeconds(double seconds) {
        this.lifetimeProductiveSeconds = Math.max(0, seconds);
    }

    public synchronized int getSecondsPerCoin() { return secondsPerCoin; }
    public synchronized void setSecondsPerCoin(int secondsPerCoin) {
        if (secondsPerCoin > 0) this.secondsPerCoin = secondsPerCoin;
    }

    public synchronized int getCoins() { return coins; }
    public synchronized double getRemainingSecondsToNextCoin() { return secondsPerCoin - totalProductiveSeconds; }
}

public class Main {
    public static void main(String[] args) {


        BlockManager blockManager = new BlockManager();
        UserEconomy economy = new UserEconomy();
        WindowMonitor monitor = new WindowMonitor(blockManager, economy);
        monitor.startMonitoring();
        new MainDashboard(blockManager, economy);

    }
}
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        BlockManager blockManager = new BlockManager();
        UserEconomy economy = new UserEconomy();

        SaveManager.load(blockManager, economy);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            SaveManager.save(blockManager, economy);
        }));

        SwingUtilities.invokeLater(() -> new MainDashboard(blockManager, economy));

        WindowMonitor monitor = new WindowMonitor(blockManager, economy);
        monitor.startMonitoring();
    }
}

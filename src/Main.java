import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private final BlockManager blockManager = new BlockManager();
    private final UserEconomy economy = new UserEconomy();
    private WindowMonitor monitor;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        SaveManager.load(blockManager, economy);
        MainDashboard dashboard = new MainDashboard(blockManager, economy);
        Scene scene = new Scene(dashboard.getView(), 1100, 720);
        scene.getStylesheets().add(getClass().getResource("/citrus.css").toExternalForm());
        stage.setTitle("Citrus — Digital Detox");
        stage.setMinWidth(900);
        stage.setMinHeight(620);
        stage.setScene(scene);
        stage.show();
        monitor = new WindowMonitor(blockManager, economy);
        monitor.startMonitoring();
    }

    @Override
    public void stop() {
        if (monitor != null) monitor.stopMonitoring();
        SaveManager.save(blockManager, economy);
    }
}

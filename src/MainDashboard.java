import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class MainDashboard {
    private final BlockManager blockManager;
    private final UserEconomy economy;
    private final BorderPane root = new BorderPane();
    private final StackPane content = new StackPane();
    private final Label coinLabel = new Label();
    private final ToggleGroup navigation = new ToggleGroup();

    public MainDashboard(BlockManager blockManager, UserEconomy economy) {
        this.blockManager = blockManager;
        this.economy = economy;
        root.getStyleClass().add("root");
        root.setLeft(createSidebar());
        StackPane mainArea = new StackPane(content, createCoinDisplay());
        root.setCenter(mainArea);
        showPage(createDashboard());
        Timeline refresh = new Timeline(new KeyFrame(Duration.seconds(1), event ->
                coinLabel.setText(String.valueOf(economy.getCoins()))));
        refresh.setCycleCount(Timeline.INDEFINITE);
        refresh.play();
    }

    public BorderPane getView() { return root; }

    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");
        HBox brandRow = new HBox(10, lemonIcon());
        brandRow.getStyleClass().add("brand-row");
        Label brand = new Label("Citrus");
        brand.getStyleClass().add("brand");
        brandRow.getChildren().add(brand);
        Label tagline = new Label("Digital detox, made simple");
        tagline.getStyleClass().add("muted");
        VBox.setMargin(tagline, new Insets(0, 0, 26, 0));
        ToggleButton dashboard = navButton("⌂  Dashboard", true, () -> showPage(createDashboard()));
        ToggleButton blocks = navButton("◫  My Blocks", false, () -> showPage(new BlockListPanel(blockManager).getView()));
        ToggleButton shop = navButton("◈  Shop", false, () -> showPage(new ShopPanel(blockManager, economy).getView()));
        ToggleButton statistics = navButton("↗  Statistics", false, () -> showPage(new StatisticsPanel(blockManager, economy).getView()));
        ToggleButton settings = navButton("⚙  Settings", false, () -> showPage(new SettingsPanel(economy).getView()));
        sidebar.getChildren().addAll(brandRow, tagline, dashboard, blocks, shop, statistics);
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        sidebar.getChildren().addAll(spacer, settings);
        return sidebar;
    }

    private ToggleButton navButton(String text, boolean selected, Runnable action) {
        ToggleButton button = new ToggleButton(text);
        button.getStyleClass().add("nav-button");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setToggleGroup(navigation);
        button.setSelected(selected);
        button.setOnAction(event -> action.run());
        return button;
    }

    private HBox createCoinDisplay() {
        HBox display = new HBox(8, coinIcon(), coinLabel);
        display.getStyleClass().add("coin-chip");
        StackPane.setAlignment(display, javafx.geometry.Pos.TOP_RIGHT);
        StackPane.setMargin(display, new Insets(22, 30, 0, 0));
        coinLabel.setText(String.valueOf(economy.getCoins()));
        return display;
    }

    private void showPage(Node page) {
        content.getChildren().setAll(page);
    }

    private Node lemonIcon() {
        Circle outer = new Circle(16, Color.web("#FDCC21"));
        Circle inner = new Circle(10, Color.web("#FFF8D9"));
        Circle center = new Circle(3, Color.web("#FDCC21"));
        return new StackPane(outer, inner, center);
    }

    private Node coinIcon() {
        Circle outer = new Circle(13, Color.web("#E5B600"));
        Circle inner = new Circle(10, Color.web("#FDCC21"));
        Label symbol = new Label("C");
        symbol.setStyle("-fx-font-size: 11px; -fx-font-weight: 800; -fx-text-fill: #8A6900;");
        return new StackPane(outer, inner, symbol);
    }

    private Node createDashboard() {
        VBox view = new VBox(22);
        view.getStyleClass().add("content");
        Label welcome = new Label("Your focus space");
        welcome.getStyleClass().add("section-title");
        Label subtitle = new Label("Set boundaries for distracting apps, then earn coins while you stay focused.");
        subtitle.getStyleClass().add("muted");
        HBox metrics = new HBox(18);
        metrics.getChildren().addAll(metricCard("Productive time", "time"), metricCard("Blocks active now", "blocks"),
                staticCard("Daily goal", "2h 0m", "A gentle target for today"));
        TutorialContent tutorial = new TutorialContent();
        view.getChildren().addAll(welcome, subtitle, metrics, tutorial.getView());
        Timeline refresh = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            int minutes = (int) economy.getTotalProductiveMinutes();
            ((Label) metrics.lookup("#time")).setText((minutes / 60) + "h " + (minutes % 60) + "m");
            ((Label) metrics.lookup("#blocks")).setText(String.valueOf(blockManager.countActiveBlocks()));
        }));
        refresh.setCycleCount(Timeline.INDEFINITE);
        refresh.play();
        return view;
    }

    private VBox metricCard(String title, String id) {
        VBox card = staticCard(title, "0", "Live update");
        ((Label) card.lookup(".metric-value")).setId(id);
        return card;
    }

    private VBox staticCard(String title, String value, String detail) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");
        card.setPrefWidth(230);
        HBox.setHgrow(card, Priority.ALWAYS);
        Label label = new Label(title);
        label.getStyleClass().add("metric-label");
        Label metric = new Label(value);
        metric.getStyleClass().add("metric-value");
        Label description = new Label(detail);
        description.getStyleClass().add("muted");
        card.getChildren().addAll(label, metric, description);
        return card;
    }
}

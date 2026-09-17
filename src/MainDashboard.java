import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainDashboard {
    private final BlockManager blockManager;
    private final UserEconomy economy;
    private final BorderPane root = new BorderPane();
    private final StackPane content = new StackPane();
    private final Label pageTitle = new Label("Dashboard");
    private final Label coinLabel = new Label();
    private final ToggleGroup navigation = new ToggleGroup();

    public MainDashboard(BlockManager blockManager, UserEconomy economy) {
        this.blockManager = blockManager;
        this.economy = economy;
        root.getStyleClass().add("root");
        root.setLeft(createSidebar());
        root.setTop(createTopBar());
        root.setCenter(content);
        showPage("Dashboard", createDashboard());
        Timeline refresh = new Timeline(new KeyFrame(Duration.seconds(1), event ->
                coinLabel.setText("🍋 " + economy.getCoins() + " coins")));
        refresh.setCycleCount(Timeline.INDEFINITE);
        refresh.play();
    }

    public BorderPane getView() { return root; }

    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");
        Label brand = new Label("Citrus");
        brand.getStyleClass().add("brand");
        Label tagline = new Label("Digital detox, made simple");
        tagline.getStyleClass().add("muted");
        VBox.setMargin(tagline, new Insets(0, 0, 26, 0));
        ToggleButton dashboard = navButton("⌂  Dashboard", true, () -> showPage("Dashboard", createDashboard()));
        ToggleButton blocks = navButton("◫  My Blocks", false, () -> showPage("My Blocks", new BlockListPanel(blockManager).getView()));
        ToggleButton shop = navButton("◈  Shop", false, () -> showPage("Shop", new ShopPanel(blockManager, economy).getView()));
        ToggleButton statistics = navButton("↗  Statistics", false, () -> showPage("Statistics", new StatisticsPanel(blockManager, economy).getView()));
        ToggleButton settings = navButton("⚙  Settings", false, () -> showPage("Settings", new SettingsPanel(economy).getView()));
        sidebar.getChildren().addAll(brand, tagline, dashboard, blocks, shop, statistics);
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

    private HBox createTopBar() {
        HBox topBar = new HBox(14);
        topBar.getStyleClass().add("top-bar");
        topBar.setAlignment(Pos.CENTER_LEFT);
        pageTitle.getStyleClass().add("page-title");
        coinLabel.getStyleClass().add("coin-chip");
        coinLabel.setText("🍋 " + economy.getCoins() + " coins");
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(pageTitle, spacer, coinLabel);
        return topBar;
    }

    private void showPage(String title, Node page) {
        pageTitle.setText(title);
        content.getChildren().setAll(page);
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

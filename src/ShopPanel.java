import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ShopPanel {
    private final BlockManager blockManager;
    private final UserEconomy economy;
    public ShopPanel(BlockManager blockManager, UserEconomy economy) { this.blockManager = blockManager; this.economy = economy; }
    public Node getView() {
        VBox root = new VBox(18); root.getStyleClass().add("content");
        Label intro = new Label("Spend your earned coins on a planned break."); intro.getStyleClass().add("muted");
        VBox card = new VBox(10); card.getStyleClass().addAll("card", "accent-card");
        Label title = new Label("YouTube pass — 10 minutes"); title.getStyleClass().add("section-title");
        Label detail = new Label("Temporarily pauses your YouTube block for 5 coins."); detail.getStyleClass().add("muted");
        Button buy = new Button("Buy for 5 coins"); buy.getStyleClass().add("primary-button");
        buy.setOnAction(event -> {
            if (economy.spendCoins(5)) { blockManager.addPass(new Pass("youtube", 10)); message("Pass purchased", "YouTube is unlocked for 10 minutes."); }
            else message("Not enough coins", "Keep focusing to earn more coins.");
        });
        card.getChildren().addAll(title, detail, buy); root.getChildren().addAll(intro, card); return root;
    }
    private void message(String title, String content) { Alert alert = new Alert(Alert.AlertType.INFORMATION, content); alert.setTitle(title); alert.setHeaderText(null); alert.showAndWait(); }
}

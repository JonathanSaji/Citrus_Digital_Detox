import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TutorialContent {
    public Node getView() {
        VBox card = new VBox(9); card.getStyleClass().addAll("card", "accent-card");
        Label title = new Label("Getting started with Citrus"); title.getStyleClass().add("section-title");
        Label steps = new Label("1. Create a block for a distracting site or app.\n2. Choose a lock that fits your routine.\n3. Stay active away from blocked apps to earn coins.\n4. Use coins for intentional short breaks.");
        steps.getStyleClass().add("muted"); card.getChildren().addAll(title, steps); return card;
    }
}

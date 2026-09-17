import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;

public class SettingsPanel {
    private final UserEconomy economy;
    public SettingsPanel(UserEconomy economy) { this.economy = economy; }
    public Node getView() {
        VBox root = new VBox(20); root.getStyleClass().add("content");
        VBox coinCard = new VBox(10); coinCard.getStyleClass().add("card");
        Label title = new Label("Coin rate"); title.getStyleClass().add("section-title");
        Label description = new Label("How many focused seconds earn one coin?"); description.getStyleClass().add("muted");
        Spinner<Integer> rate = new Spinner<>(60, 3600, economy.getSecondsPerCoin(), 30);
        Button save = new Button("Save coin rate"); save.getStyleClass().add("primary-button"); save.setOnAction(event -> economy.setSecondsPerCoin(rate.getValue()));
        coinCard.getChildren().addAll(title, description, rate, save);
        VBox tutorial = new VBox(8); tutorial.getStyleClass().add("card"); Label tutorialTitle = new Label("How to use Citrus"); tutorialTitle.getStyleClass().add("section-title"); tutorial.getChildren().addAll(tutorialTitle, new TutorialContent().getView());
        VBox music = new VBox(8); music.getStyleClass().add("card"); Label musicTitle = new Label("Chill background music"); musicTitle.getStyleClass().add("section-title"); Label coming = new Label("Coming soon — a calm focus soundtrack player will live here."); coming.getStyleClass().add("muted"); music.getChildren().addAll(musicTitle, coming);
        root.getChildren().addAll(coinCard, tutorial, music); return root;
    }
}

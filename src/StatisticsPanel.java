import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class StatisticsPanel {
    private final BlockManager blockManager;
    private final UserEconomy economy;
    private final ProgressBar progress = new ProgressBar();
    private final TableView<Block> table = new TableView<>();
    public StatisticsPanel(BlockManager blockManager, UserEconomy economy) { this.blockManager = blockManager; this.economy = economy; configureTable(); }
    public Node getView() {
        VBox root = new VBox(18); root.getStyleClass().add("content");
        Label goal = new Label("Daily focus goal"); goal.getStyleClass().add("section-title"); progress.setMaxWidth(Double.MAX_VALUE);
        Label hint = new Label("Progress toward 2 hours of productive time"); hint.getStyleClass().add("muted");
        root.getChildren().addAll(goal, progress, hint, table); refresh();
        Timeline refresh = new Timeline(new KeyFrame(Duration.seconds(1), e -> refresh())); refresh.setCycleCount(Timeline.INDEFINITE); refresh.play(); return root;
    }
    private void configureTable() {
        TableColumn<Block, String> target = new TableColumn<>("Target"); target.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTargetName()));
        TableColumn<Block, String> type = new TableColumn<>("Lock type"); type.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLockType().toString()));
        TableColumn<Block, String> triggers = new TableColumn<>("Times triggered"); triggers.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getTimesTriggered())));
        TableColumn<Block, String> status = new TableColumn<>("Status"); status.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().isActive() ? "Active" : "Inactive"));
        table.getColumns().addAll(target, type, triggers, status); table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    private void refresh() { progress.setProgress(Math.min(economy.getTotalProductiveMinutes() / 120.0, 1)); table.setItems(FXCollections.observableArrayList(blockManager.getBlocks())); }
}

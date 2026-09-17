import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumSet;

public class BlockDialog extends Dialog<Block> {
    private final TextField name = new TextField();
    private final ComboBox<LockType> type = new ComboBox<>(FXCollections.observableArrayList(LockType.values()));
    private final VBox options = new VBox(10);
    private final Spinner<Integer> timer = new Spinner<>(1, 1440, 10);
    private final Spinner<Integer> rangeStart = new Spinner<>(0, 23, 9);
    private final Spinner<Integer> rangeEnd = new Spinner<>(0, 23, 17);
    private final Spinner<Integer> bedStart = new Spinner<>(0, 23, 22);
    private final Spinner<Integer> bedEnd = new Spinner<>(0, 23, 6);
    private final Spinner<Integer> challenge = new Spinner<>(5, 200, 20, 5);
    private final Spinner<Integer> delay = new Spinner<>(5, 600, 30, 5);
    private final PasswordField password = new PasswordField();
    private final CheckBox[] days = new CheckBox[7];

    public BlockDialog(BlockManager blockManager) {
        setTitle("Create a block");
        setHeaderText("Choose what Citrus should help you avoid.");
        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        type.setValue(LockType.TIMER);
        type.valueProperty().addListener((obs, oldType, newType) -> refreshOptions());
        GridPane form = new GridPane();
        form.setHgap(12); form.setVgap(12); form.setPadding(new Insets(10));
        form.addRow(0, new Label("Website or app"), name);
        form.addRow(1, new Label("Lock type"), type);
        form.add(options, 0, 2, 2, 1);
        getDialogPane().setContent(form);
        refreshOptions();
        setResultConverter(button -> button == ButtonType.OK ? createBlock() : null);
    }

    private void refreshOptions() {
        options.getChildren().clear();
        switch (type.getValue()) {
            case TIMER -> options.getChildren().add(row("Duration (minutes)", timer));
            case TIME_RANGE -> {
                options.getChildren().addAll(row("Start hour", rangeStart), row("End hour", rangeEnd));
                HBox dayRow = new HBox(6); dayRow.getChildren().add(new Label("Days:"));
                for (DayOfWeek day : DayOfWeek.values()) {
                    CheckBox box = new CheckBox(day.name().substring(0, 3)); box.setSelected(true);
                    days[day.getValue() - 1] = box; dayRow.getChildren().add(box);
                }
                options.getChildren().add(dayRow);
            }
            case BEDTIME -> options.getChildren().addAll(row("Sleep hour", bedStart), row("Wake hour", bedEnd));
            case RANDOM_TEXT -> options.getChildren().add(row("Different words", challenge));
            case DELAY -> options.getChildren().add(row("Delay (seconds)", delay));
            case EMERGENCY -> options.getChildren().add(row("Emergency password", password));
        }
    }

    private HBox row(String label, javafx.scene.Node field) { return new HBox(12, new Label(label), field); }

    private Block createBlock() {
        if (name.getText().isBlank()) { warning("Enter a website or app name."); return null; }
        LockType selected = type.getValue();
        Block block = new Block(name.getText().trim(), selected);
        switch (selected) {
            case TIMER -> block.setUnlockAt(LocalDateTime.now().plusMinutes(timer.getValue()));
            case TIME_RANGE -> {
                if (rangeStart.getValue().equals(rangeEnd.getValue())) { warning("Start and end hours must be different."); return null; }
                block.setRangeStart(LocalTime.of(rangeStart.getValue(), 0)); block.setRangeEnd(LocalTime.of(rangeEnd.getValue(), 0));
                EnumSet<DayOfWeek> selectedDays = EnumSet.noneOf(DayOfWeek.class);
                for (DayOfWeek day : DayOfWeek.values()) if (days[day.getValue() - 1].isSelected()) selectedDays.add(day);
                if (selectedDays.isEmpty()) { warning("Select at least one active day."); return null; }
                block.setActiveDays(selectedDays);
            }
            case BEDTIME -> { block.setBedStart(LocalTime.of(bedStart.getValue(), 0)); block.setBedEnd(LocalTime.of(bedEnd.getValue(), 0)); }
            case RANDOM_TEXT -> block.setChallengeLength(challenge.getValue());
            case DELAY -> block.setDelaySeconds(delay.getValue());
            case EMERGENCY -> { if (password.getText().isBlank()) { warning("Enter an emergency password."); return null; } block.setEmergencyPassword(password.getText()); }
        }
        return block;
    }

    private void warning(String message) { new Alert(Alert.AlertType.WARNING, message, ButtonType.OK).showAndWait(); }
}

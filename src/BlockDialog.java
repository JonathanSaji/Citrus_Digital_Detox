import javax.swing.*;
import java.awt.*;

public class BlockDialog extends JDialog {
    private BlockManager blockManager;
    private JTextField nameField;
    private JComboBox<LockType> lockTypeBox;

    private CardLayout dynamicCardLayout;
    private JPanel dynamicPanel;

    // user input for locks
    private JSpinner timerMinutesSpinner;
    private JSpinner rangeStartSpinner;
    private JSpinner rangeEndSpinner;
    private JSpinner bedStartSpinner;
    private JSpinner bedEndSpinner;
    private JSpinner challengeLengthSpinner;
    private JSpinner delaySecondsSpinner;
    private JPasswordField emergencyPasswordField;

    public BlockDialog(BlockManager blockManager) {
        this.blockManager = blockManager;
        setTitle("Create Block");
        setSize(350, 250);
        setModal(true);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        add(topPanel, BorderLayout.NORTH);
        getContentPane().setBackground(new Color(253, 204, 33));

        dynamicCardLayout = new CardLayout();
        dynamicPanel = new JPanel(dynamicCardLayout);

        JPanel timerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        timerPanel.add(new JLabel("Duration (minutes):"));
        timerMinutesSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 1440, 1));
        timerPanel.add(timerMinutesSpinner);
        dynamicPanel.add(timerPanel, "TIMER");

        JPanel rangePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        rangePanel.add(new JLabel("Start Hour (0-23):"));
        rangeStartSpinner = new JSpinner(new SpinnerNumberModel(9, 0, 23, 1));
        rangePanel.add(rangeStartSpinner);
        rangePanel.add(new JLabel("End Hour (0-23):"));
        rangeEndSpinner = new JSpinner(new SpinnerNumberModel(17, 0, 23, 1));
        rangePanel.add(rangeEndSpinner);
        dynamicPanel.add(rangePanel, "TIME_RANGE");

        JPanel textPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        textPanel.add(new JLabel("Characters to type:"));
        challengeLengthSpinner = new JSpinner(new SpinnerNumberModel(20, 5, 200, 5));
        textPanel.add(challengeLengthSpinner);
        dynamicPanel.add(textPanel, "RANDOM_TEXT");

        JPanel delayPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        delayPanel.add(new JLabel("Delay (seconds):"));
        delaySecondsSpinner = new JSpinner(new SpinnerNumberModel(30, 5, 600, 5));
        delayPanel.add(delaySecondsSpinner);
        dynamicPanel.add(delayPanel, "DELAY");

        JPanel bedtimePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        bedtimePanel.add(new JLabel("Sleep Hour (0-23):"));
        bedStartSpinner = new JSpinner(new SpinnerNumberModel(22, 0, 23, 1));
        bedtimePanel.add(bedStartSpinner);
        bedtimePanel.add(new JLabel("Wake Hour (0-23):"));
        bedEndSpinner = new JSpinner(new SpinnerNumberModel(6, 0, 23, 1));
        bedtimePanel.add(bedEndSpinner);
        dynamicPanel.add(bedtimePanel, "BEDTIME");

        JPanel emergencyPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        emergencyPanel.add(new JLabel("Password:"));
        emergencyPasswordField = new JPasswordField();
        emergencyPanel.add(emergencyPasswordField);
        dynamicPanel.add(emergencyPanel, "EMERGENCY");

        add(dynamicPanel, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save");
        add(saveButton, BorderLayout.SOUTH);

        topPanel.add(new JLabel("Website/App:"));
        nameField = new JTextField();
        topPanel.add(nameField);

        topPanel.add(new JLabel("Lock Type:"));
        lockTypeBox = new JComboBox<>(LockType.values());

        lockTypeBox.addActionListener(e -> {
            LockType selected = (LockType) lockTypeBox.getSelectedItem();
            dynamicCardLayout.show(dynamicPanel, selected.name());

        });
        topPanel.add(lockTypeBox);

        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            LockType type = (LockType) lockTypeBox.getSelectedItem();
            if (name.isEmpty()) return;

            Block block = new Block(name, type);

            switch (type) {
                case TIMER:
                    int minutes = (int) timerMinutesSpinner.getValue();
                    block.setUnlockAt(java.time.LocalDateTime.now().plusMinutes(minutes));
                    break;
                case TIME_RANGE:
                    int startHour = (int) rangeStartSpinner.getValue();
                    int endHour = (int) rangeEndSpinner.getValue();
                    block.setRangeStart(java.time.LocalTime.of(startHour, 0));
                    block.setRangeEnd(java.time.LocalTime.of(endHour, 0));
                    break;
                case BEDTIME:
                    int bedHour = (int) bedStartSpinner.getValue();
                    int wakeHour = (int) bedEndSpinner.getValue();
                    block.setBedStart(java.time.LocalTime.of(bedHour, 0));
                    block.setBedEnd(java.time.LocalTime.of(wakeHour, 0));
                    break;
                case RANDOM_TEXT:
                    block.setChallengeLength((int) challengeLengthSpinner.getValue());
                    break;
                case DELAY:
                    block.setDelaySeconds((int) delaySecondsSpinner.getValue());
                    break;
                case EMERGENCY:
                    block.setEmergencyPassword(new String(emergencyPasswordField.getPassword()));
                    break;
            }

            blockManager.addBlock(block);
            dispose();
        });

        setVisible(true);
    }
}
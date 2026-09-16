import javax.swing.*;
import java.awt.*;
import java.awt.GridBagConstraints;



public class BlockOverlay extends JFrame {

    private Block block;
    private BlockManager blockManager;

    public BlockOverlay(Block block, BlockManager blockManager) {
        this.block = block;
        this.blockManager = blockManager;

        String targetName = block.getTargetName();
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle(targetName);
        setAlwaysOnTop(true);
        getContentPane().setBackground(new Color(20, 20, 20));

        setLayout(new GridBagLayout());
        JLabel messageLabel = new JLabel("🍋 " + targetName + " is blocked");
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        messageLabel.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 15, 0);
        add(messageLabel, gbc);

        JLabel subLabel = new JLabel("Stay focused — this will unlock automatically.");
        subLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subLabel.setForeground(new Color(180, 180, 180));
        gbc.gridy = 1;
        add(subLabel, gbc);

        addUnlockControls();

        setVisible(true);
    }
    private void addUnlockControls() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 0, 0);

        if (block.getLockType() == LockType.EMERGENCY) {
            JPasswordField pwField = new JPasswordField(15);
            JButton submitButton = new JButton("Unlock");
            submitButton.addActionListener(e -> {
                String entered = new String(pwField.getPassword());
                if (entered.equals(block.getEmergencyPassword())) {
                    blockManager.addPass(new Pass(block.getTargetName(), 10));
                    dispose();
                }
            });
            add(pwField, gbc);
            gbc.gridy = 3;
            add(submitButton, gbc);

        } else if (block.getLockType() == LockType.DELAY) {
            JLabel countdownLabel = new JLabel("Please wait...");
            countdownLabel.setForeground(Color.WHITE);
            add(countdownLabel, gbc);

            javax.swing.Timer delayTimer = new javax.swing.Timer(block.getDelaySeconds() * 1000, e -> {
                blockManager.addPass(new Pass(block.getTargetName(), 10));
                dispose();
            });
            delayTimer.setRepeats(false);
            delayTimer.start();

        } else if (block.getLockType() == LockType.RANDOM_TEXT) {
            JLabel instructionLabel = new JLabel("Type " + block.getChallengeLength() + " different words (2+ letters each, space-separated):");
            instructionLabel.setForeground(Color.WHITE);
            JTextField inputField = new JTextField(25);
            JLabel errorLabel = new JLabel(" ");
            errorLabel.setForeground(new Color(230, 80, 80));
            JButton checkButton = new JButton("Submit");

            checkButton.addActionListener(e -> {
                if (block.validateRandomTextInput(inputField.getText())) {
                    blockManager.addPass(new Pass(block.getTargetName(), 10));
                    dispose();
                } else {
                    errorLabel.setText("Try again — check word count, length, and duplicates.");
                }
            });

            add(instructionLabel, gbc);
            gbc.gridy = 3;
            add(inputField, gbc);
            gbc.gridy = 4;
            add(checkButton, gbc);
            gbc.gridy = 5;
            add(errorLabel, gbc);
        }
    }

}

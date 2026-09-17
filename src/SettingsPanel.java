import javax.swing.*;
import java.awt.*;

/**
 * A shell-level settings destination so the shared header remains mounted while
 * users move between every primary section of the application.
 */
public class SettingsPanel extends JPanel {
    public SettingsPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(253, 204, 33));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(20, 20, 20));
        card.setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));

        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel descriptionLabel = new JLabel("Application preferences will appear here.");
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descriptionLabel.setForeground(new Color(180, 180, 180));

        card.add(titleLabel);
        card.add(descriptionLabel);
        add(card);
    }
}

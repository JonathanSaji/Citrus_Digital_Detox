import javax.swing.*;
import java.awt.*;

public class StatisticsPanel extends JPanel {
    private BlockManager blockManager;
    private UserEconomy economy;

    public StatisticsPanel(BlockManager blockManager, UserEconomy economy) {
        this.blockManager = blockManager;
        this.economy = economy;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(253, 204, 33));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
}
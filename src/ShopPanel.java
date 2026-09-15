import javax.swing.*;
import java.awt.*;

public class ShopPanel extends JPanel {
    private BlockManager blockManager;
    private UserEconomy economy;

    public ShopPanel(BlockManager blockManager, UserEconomy economy) {
        this.blockManager = blockManager;
        this.economy = economy;
        setLayout(new GridLayout(2, 1, 20, 20));
        setBackground(new Color(253, 204, 33));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel passCard = new JPanel(new GridLayout(3, 1, 5, 5));
        passCard.setBackground(new Color(20, 20, 20));
        passCard.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("YouTube Pass — 10 min");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel priceLabel = new JLabel("Cost: 5 coins");
        priceLabel.setForeground(new Color(180, 180, 180));

        JButton buyButton = new JButton("Buy");
        buyButton.setBackground(new Color(230, 180, 20));

        passCard.add(titleLabel);
        passCard.add(priceLabel);
        passCard.add(buyButton);
        add(passCard);

        buyButton.addActionListener(e -> {
            if (economy.spendCoins(5)) {
                blockManager.addPass(new Pass("youtube", 10));
                JOptionPane.showMessageDialog(this, "Pass purchased! 10 minutes unlocked.");
            } else {
                JOptionPane.showMessageDialog(this, "Not enough coins!");
            }
        });
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainDashboard extends JFrame {
    private UserEconomy economy;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private JLabel coinLabel;
    private BlockManager blockManager;

    public MainDashboard(BlockManager blockManager, UserEconomy economy) {
        this.economy = economy;
        this.blockManager = blockManager;

        // Window setup
        setTitle("Citrus - Digital Detox");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar Navigation
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(6, 1, 10, 10));
        sidebar.setBackground(new Color(230, 180, 20));
        sidebar.setPreferredSize(new Dimension(200, 600));

        JLabel titleLabel = new JLabel(" Citrus", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        sidebar.add(titleLabel);

        // Navigation Buttons
        JButton btnDashboard = createNavButton("Dashboard");
        JButton btnBlockList = createNavButton("Block List");
        JButton btnShop = createNavButton("Shop");
        JButton btnStats = createNavButton("Statistics");

        sidebar.add(btnDashboard);
        sidebar.add(btnBlockList);
        sidebar.add(btnShop);
        sidebar.add(btnStats);

        // Main Content Area (CardLayout switches views)
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(new Color(253, 204, 33));

        mainContentPanel.add(createDashboardPanel(), "Dashboard");
        mainContentPanel.add(new BlockListPanel(blockManager),"Block List");
        mainContentPanel.add(new ShopPanel(blockManager, economy), "Shop");
        mainContentPanel.add(new StatisticsPanel(blockManager, economy), "Statistics");

        // Top Bar (Coin Balance)
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        topBar.setBackground(new Color(230, 180, 20));
        coinLabel = new JLabel("coins: " + economy.getCoins());
        coinLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        topBar.add(coinLabel);

        // Add action listeners to switch screens
        btnDashboard.addActionListener(e -> cardLayout.show(mainContentPanel, "Dashboard"));
        btnBlockList.addActionListener(e -> cardLayout.show(mainContentPanel, "Block List"));
        btnShop.addActionListener(e -> cardLayout.show(mainContentPanel, "Shop"));
        btnStats.addActionListener(e -> cardLayout.show(mainContentPanel, "Statistics"));


        // Layout Assembly
        add(sidebar, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);

        javax.swing.Timer coinRefreshTimer = new javax.swing.Timer(1000, e -> {
            coinLabel.setText("coins: " + economy.getCoins());
        });
        coinRefreshTimer.start();

        setVisible(true);
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(20, 20, 20));
        button.setForeground(new Color(180, 180, 180));
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return button;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBackground(new Color(253, 204, 33));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel productiveValueLabel = new JLabel("0h 0m", SwingConstants.CENTER);
        JLabel activeBlocksValueLabel = new JLabel("0", SwingConstants.CENTER);

        panel.add(createLiveCard("Productive Time", productiveValueLabel));
        panel.add(createLiveCard("Active Blocks", activeBlocksValueLabel));
        panel.add(createCard("Current Streak", "1 Day 🔥"));
        panel.add(createCard("Daily Goal", "2h 0m Target"));

        javax.swing.Timer dashboardRefreshTimer = new javax.swing.Timer(1000, e -> {
            int totalMinutes = (int) economy.getTotalProductiveMinutes();
            int hours = totalMinutes / 60;
            int minutes = totalMinutes % 60;
            productiveValueLabel.setText(hours + "h " + minutes + "m");
            activeBlocksValueLabel.setText(String.valueOf(blockManager.countActiveBlocks()));
        });
        dashboardRefreshTimer.start();

        return panel;
    }

    private JPanel createLiveCard(String title, JLabel valLbl) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(20, 20, 20));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        titleLbl.setForeground(new Color(180, 180, 180));

        valLbl.setFont(new Font("SansSerif", Font.BOLD, 26));
        valLbl.setForeground(Color.WHITE);

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valLbl, BorderLayout.CENTER);
        return card;
    }

    private JPanel createCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(20, 20, 20)); // Black card background
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        titleLbl.setForeground(new Color(180, 180, 180)); // Soft gray text

        JLabel valLbl = new JLabel(value, SwingConstants.CENTER);
        valLbl.setFont(new Font("SansSerif", Font.BOLD, 26));
        valLbl.setForeground(Color.WHITE); // Bold white text

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valLbl, BorderLayout.CENTER);
        return card;
    }
    private JPanel createDummyPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel label = new JLabel(title);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(label);
        return panel;
    }

    }

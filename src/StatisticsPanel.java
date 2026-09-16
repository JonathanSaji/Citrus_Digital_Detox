import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
public class StatisticsPanel extends JPanel {
    private BlockManager blockManager;
    private UserEconomy economy;
    private DefaultTableModel tableModel;
    private JPanel proportionBar;

    public StatisticsPanel(BlockManager blockManager, UserEconomy economy) {
        this.blockManager = blockManager;
        this.economy = economy;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(253, 204, 33));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(createProportionBar(), BorderLayout.NORTH);
        add(createTriggerTable(), BorderLayout.CENTER);

        javax.swing.Timer statsRefreshTimer = new javax.swing.Timer(1000, e -> {
            refreshTable();
            proportionBar.repaint();
        });
        statsRefreshTimer.start();

    }

    private JPanel createProportionBar() {
        proportionBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                double goalMinutes = 120.0;
                double ratio = Math.min(economy.getTotalProductiveMinutes() / goalMinutes, 1.0);
                int filledWidth = (int) (getWidth() * ratio);
                g.setColor(new Color(20, 20, 20));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(new Color(230, 180, 20));
                g.fillRect(0, 0, filledWidth, getHeight());
            }
        };
        proportionBar.setPreferredSize(new Dimension(400, 30));
        return proportionBar;
    }

    private JScrollPane createTriggerTable() {
        String[] columns = {"Target", "Lock Type", "Times Triggered", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        refreshTable();

        JTable table = new JTable(tableModel);
        table.setBackground(new Color(20, 20, 20));
        table.setForeground(Color.WHITE);
        table.setRowHeight(28);
        table.getTableHeader().setBackground(new Color(230, 180, 20));
        table.getTableHeader().setForeground(Color.BLACK);
        table.setEnabled(false);

        return new JScrollPane(table);
    }
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Block b : blockManager.getBlocks()) {
            tableModel.addRow(new Object[]{
                    b.getTargetName(),
                    b.getLockType(),
                    b.getTimesTriggered(),
                    b.isActive() ? "Active" : "Inactive"
            });
        }
    }
}

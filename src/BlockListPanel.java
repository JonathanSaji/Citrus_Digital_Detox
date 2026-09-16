import javax.swing.*;
import java.awt.*;

public class BlockListPanel extends JPanel {
    private BlockManager blockManager;
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> blockList;
    public BlockListPanel(BlockManager blockManager) {
        this.blockManager = blockManager;
        blockList = new JList<>(listModel);
        setLayout(new BorderLayout());
        setBackground(new Color(253, 204, 33));

        javax.swing.Timer listRefreshTimer = new javax.swing.Timer(1000, e -> refreshList());
        listRefreshTimer.start();

        for (Block b : blockManager.getBlocks()) {
            String status = b.isActive() ? "[Active]" : "[Inactive]";
            listModel.addElement(status + " " + b.getTargetName() + " (" + b.getLockType() + ")");
        }

        blockList = new JList<>(listModel);
        blockList.setBackground(new Color(20, 20, 20));
        blockList.setForeground(Color.WHITE);
        blockList.setFont(new Font("SansSerif", Font.PLAIN, 16));

        add(new JScrollPane(blockList), BorderLayout.CENTER);

        JButton createButton = new JButton("+ Create Block");
        createButton.setBackground(new Color(20, 20, 20));
        createButton.setForeground(Color.WHITE);
        createButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        createButton.setFocusPainted(false);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.setBackground(new Color(253, 204, 33));
        buttonPanel.add(createButton);

        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.setBackground(new Color(20, 20, 20));
        deleteButton.setForeground(Color.WHITE);
        buttonPanel.add(deleteButton);

        JButton toggleButton = new JButton("Toggle Active");
        toggleButton.setBackground(new Color(20, 20, 20));
        toggleButton.setForeground(Color.WHITE);
        buttonPanel.add(toggleButton);

        add(buttonPanel, BorderLayout.SOUTH);

        createButton.addActionListener(e -> {
            new BlockDialog(blockManager);
            refreshList();
        });

        deleteButton.addActionListener(e -> {
            int index = blockList.getSelectedIndex();
            if (index != -1) {
                blockManager.removeBlock(blockManager.getBlocks().get(index));
                refreshList();
            }
        });

        toggleButton.addActionListener(e -> {
            int index = blockList.getSelectedIndex();
            if (index != -1) {
                Block b = blockManager.getBlocks().get(index);
                b.setActive(!b.isActive());
                refreshList();
            }
        });
    }
    private void refreshList() {
        listModel.clear();
        for (Block b : blockManager.getBlocks()) {
            String enabled = b.isActive() ? "[Enabled]" : "[Disabled]";
            String live = b.isCurrentlyBlocking() ? "[Blocking Now]" : "[Not Blocking]";
            listModel.addElement(enabled + " " + live + " " + b.getTargetName() + " (" + b.getLockType() + ")");
        }
    }
}

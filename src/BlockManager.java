import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BlockManager {
    private final List<Block> blocks = new CopyOnWriteArrayList<>();
    private final List<Pass> passes = new CopyOnWriteArrayList<>();

    public void addPass(Pass pass) {
        passes.add(pass);
    }
    private boolean hasActivePass(String targetName) {
        passes.removeIf(pass -> !pass.isActive());
        for (Pass p : passes) {
            if (p.isActive() && p.getTargetName().equalsIgnoreCase(targetName)) {
                return true;
            }
        }
        return false;
    }
    public void addBlock(Block block) {
        blocks.add(block);
    }

    public void removeBlock(Block block) {
        blocks.remove(block);
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public int countActiveBlocks() {
        int count = 0;
        for (Block b : blocks) {
            if (b.isCurrentlyBlocking()) count++;
        }
        return count;
    }
    /**
     * Given the active window's title (e.g. "YouTube - Google Chrome"),
     * return the matching Block if one should trigger an overlay, else null.
     */
    public Block findMatchingBlock(String activeWindowTitle) {
        if (activeWindowTitle == null || activeWindowTitle.isBlank()) return null;
        String lower = activeWindowTitle.toLowerCase();

        for (Block b : blocks) {
            if (b.isCurrentlyBlocking()
                    && lower.contains(b.getTargetName().toLowerCase())
                    && !hasActivePass(b.getTargetName())) {
                return b;
            }
        }
        return null;
    }

}

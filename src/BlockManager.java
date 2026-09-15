import java.util.ArrayList;
import java.util.List;

public class BlockManager {
    private List<Block> blocks = new ArrayList<>();
    private List<Pass> passes = new ArrayList<>();

    public void addPass(Pass pass) {
        passes.add(pass);
    }
    private boolean hasActivePass(String targetName) {
        for (Pass p : passes) {
            if (p.isActive() && targetName.toLowerCase().contains(p.getTargetName().toLowerCase())) {
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
        if (activeWindowTitle == null) return null;
        String lower = activeWindowTitle.toLowerCase();

        for (Block b : blocks) {
            if (b.isCurrentlyBlocking() && lower.contains(b.getTargetName().toLowerCase()) && !hasActivePass(b.getTargetName())) {                return b;
            }
        }
        return null;
    }

}

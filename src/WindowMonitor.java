import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import java.util.Timer;
import java.util.TimerTask;

public class WindowMonitor {
    private BlockManager blockManager;
    private BlockOverlay currentOverlay = null;
    private UserEconomy economy;
    private long lastTickTime = System.currentTimeMillis();

    public WindowMonitor(BlockManager blockManager, UserEconomy economy) {
        this.blockManager = blockManager;
        this.economy = economy;
    }
    public String getActiveWindowTitle() {
        char[] buffer = new char[1024]; // buffer to store the window title
        User32 user32 = User32.INSTANCE;   // asks windows to keep track of whats on focus
        HWND hwnd = user32.GetForegroundWindow(); // get the handle of the active window
        user32.GetWindowText(hwnd, buffer, 1024); // get the title of the active window
        return Native.toString(buffer); // convert the title to a string
    }
    public void startMonitoring() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                String title = getActiveWindowTitle();
                Block match = blockManager.findMatchingBlock(title);
                if (match != null && currentOverlay == null) {
                    match.incrementTriggerCount();
                    System.out.println(match.getTargetName() + " triggered " + match.getTimesTriggered() + " times");
                    currentOverlay = new BlockOverlay(match, blockManager);
                }
                else if (match == null && currentOverlay!=null){
                    currentOverlay.dispose();
                    currentOverlay = null;
                }
                long now = System.currentTimeMillis();
                double elapsedSeconds = (now - lastTickTime) / 1000.0;
                lastTickTime = now;

                if (match == null && getIdleSeconds() < 60) {
                    economy.addProductiveTime(elapsedSeconds);
                }
            }
        }, 0, 300);
    }
    public long getIdleSeconds() {
        User32.LASTINPUTINFO lastInputInfo = new User32.LASTINPUTINFO(); // asks windows to keep track of idle time
        lastInputInfo.cbSize = lastInputInfo.size(); // size of the structure
        User32.INSTANCE.GetLastInputInfo(lastInputInfo); // get the last input time

        int lastInputTick = lastInputInfo.dwTime; //
        int currentTick = Kernel32.INSTANCE.GetTickCount();
        return (currentTick - lastInputTick) / 1000L;
    }


}
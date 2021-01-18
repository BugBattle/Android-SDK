package bugbattle.io.bugbattle.model;

import android.graphics.Bitmap;

import java.util.Date;

public class Replay {
    private ScreenshotReplay[] screenshots;
    private int tick = 1000;
    private int ringBufferCounter = 0;

    /**
     * The timespan of the replay is calculated with numberOfScreenshots * tick. (result in ms)
     * @param numberOfScreenshots number of screenshots, after end reached, the old ones are overridden.
     * @param tick value in ms
     */
    public Replay(int numberOfScreenshots, int tick) {
        screenshots = new ScreenshotReplay[numberOfScreenshots];
        this.tick = tick;
    }

    public void addScreenshot(Bitmap bitmap) {
        if(ringBufferCounter > screenshots.length-1) {
           ringBufferCounter = 0;
        }
        screenshots[ringBufferCounter++] = new ScreenshotReplay(bitmap, new Date());
    }

    public void reset() {
        ringBufferCounter = 0;
        screenshots = new ScreenshotReplay[screenshots.length];
    }

    public ScreenshotReplay[] getScreenshots() {
        return this.screenshots;
    }

    public int getTick() {
        return this.tick;
    }
}

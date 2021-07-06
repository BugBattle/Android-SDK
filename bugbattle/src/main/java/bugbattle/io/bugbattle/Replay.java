package bugbattle.io.bugbattle;

import android.graphics.Bitmap;

import java.util.Date;

class Replay {
    private ScreenshotReplay[] screenshots;
    private final int interval;
    private int ringBufferCounter = 0;

    /**
     * The timespan of the replay is calculated with numberOfScreenshots * tick. (result in ms)
     *
     * @param numberOfScreenshots number of screenshots, after end reached, the old ones are overridden.
     * @param interval            value in ms
     */
    public Replay(int numberOfScreenshots, int interval) {
        screenshots = new ScreenshotReplay[numberOfScreenshots];
        this.interval = interval;
    }

    public void addScreenshot(Bitmap bitmap, String screenName) {
        if (ringBufferCounter > screenshots.length - 1) {
            ringBufferCounter = 0;
        }
        screenshots[ringBufferCounter++] = new ScreenshotReplay(bitmap, screenName, new Date());
    }

    public void addInteractionToCurrentReplay(Interaction interaction) {
        int currentIndex = ringBufferCounter;
        if (ringBufferCounter == 0) {
            currentIndex = ringBufferCounter + 1;
        }
        screenshots[currentIndex - 1].addInteraction(interaction);
    }

    public void reset() {
        ringBufferCounter = 0;
        screenshots = new ScreenshotReplay[screenshots.length];
    }

    public ScreenshotReplay[] getScreenshots() {
        return this.screenshots;
    }

    public int getInterval() {
        return this.interval;
    }
}

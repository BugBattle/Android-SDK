package bugbattle.io.bugbattle.model;

import android.graphics.Bitmap;

import java.util.Date;

public class Replay {
    private ScreenshotReplay[] screenshots;
    private Date startedAt;
    private int interval;
    private int ringBufferCounter = 0;
    private String currentActivity = "";

    /**
     * The timespan of the replay is calculated with numberOfScreenshots * tick. (result in ms)
     *
     * @param numberOfScreenshots number of screenshots, after end reached, the old ones are overridden.
     * @param interval            value in ms
     */
    public Replay(int numberOfScreenshots, int interval) {
        screenshots = new ScreenshotReplay[numberOfScreenshots];
        this.interval = interval;
        startedAt = new Date();
    }

    public void addScreenshot(Bitmap bitmap) {
        if (ringBufferCounter > screenshots.length - 1) {
            ringBufferCounter = 0;
        }
        if (ringBufferCounter == 0) {
            startedAt = new Date();
        }
        screenshots[ringBufferCounter++] = new ScreenshotReplay(bitmap, currentActivity, new Date());
    }

    public void reset() {
        ringBufferCounter = 0;
        screenshots = new ScreenshotReplay[screenshots.length];
    }

    public void addInteractionToCurrentScreenshot(Interaction interaction) {
        System.out.println(interaction);
        if (ringBufferCounter == 0) {
            return;
        }
        if (screenshots[ringBufferCounter - 1] != null) {
            screenshots[ringBufferCounter - 1].addInteraction(interaction);
        }
    }

    public ScreenshotReplay[] getScreenshots() {
        return this.screenshots;
    }

    public int getInterval() {
        return this.interval;
    }

    public void setCurrentActivity(String currentActivity) {
        this.currentActivity = currentActivity;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public float getOffsetToDate() {
        if (startedAt == null) {
            return 0;
        }
        return new Date().getTime() - startedAt.getTime();
    }
}

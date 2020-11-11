package bugbattle.io.bugbattle.service;

import android.app.Activity;

public abstract class BBDetector {

    public Activity activity;
    private ScreenshotTaker screenshotTaker;

    /**
     * Abstract class for Detectors. All implemented detectors must extend
     * this class.
     *
     * @param activity activity for access app
     */
    public BBDetector(Activity activity) {
        this.activity = activity;
        screenshotTaker = new ScreenshotTaker();
    }

    public abstract void initialize();

    public abstract void resume();

    public abstract void pause();

    public void takeScreenshot() {
        screenshotTaker.takeScreenshot();
    }
}

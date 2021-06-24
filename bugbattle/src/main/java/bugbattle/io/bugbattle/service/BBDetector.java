package bugbattle.io.bugbattle.service;

import android.app.Application;
import android.content.Context;

import bugbattle.io.bugbattle.model.BugBattleConfig;

/**
 * All methods to activate BB must include this abstract class
 */
public abstract class BBDetector {

    public Context context;
    public Application application;
    private ScreenshotTaker screenshotTaker;

    /**
     * Abstract class for Detectors. All implemented detectors must extend
     * this class.
     *
     * @param application application for access app
     */
    public BBDetector(Application application) {
        this.context = application.getApplicationContext();
        this.application = application;
        screenshotTaker = new ScreenshotTaker();
    }

    public abstract void initialize();

    public abstract void resume();

    public abstract void pause();

    public void takeScreenshot() {
        if (BugBattleConfig.getInstance().getBugWillBeSentCallback() != null) {
            BugBattleConfig.getInstance().getBugWillBeSentCallback().flowInvoced();
        }
        screenshotTaker.takeScreenshot();
    }
}

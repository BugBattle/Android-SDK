package bugbattle.io.bugbattle.service;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Handler;

import bugbattle.io.bugbattle.model.FeedbackModel;
import bugbattle.io.bugbattle.model.Replay;
import bugbattle.io.bugbattle.util.ScreenshotUtil;

public class ReplaysDetector extends BBDetector {
    private Replay replay;
    private Handler handler;
    /**
     * Abstract class for Detectors. All implemented detectors must extend
     * this class.
     *
     * @param application application for access app
     */
    public ReplaysDetector(Application application) {
        super(application);
    }

    @Override
    public void initialize() {
        replay = FeedbackModel.getInstance().getReplay();
        handler = new Handler();
        //start
        handler.post(runnableCode);
    }

    @Override
    public void resume() {
        handler.post(runnableCode);
    }

    @Override
    public void pause() {
        handler.removeCallbacks(runnableCode);
    }

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            Bitmap bitmap = ScreenshotUtil.takeScreenshot(0.4f);
            replay.addScreenshot(bitmap);
            handler.postDelayed(this, replay.getTick());
        }
    };
}

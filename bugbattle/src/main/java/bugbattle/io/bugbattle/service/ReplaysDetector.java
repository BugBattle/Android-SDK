package bugbattle.io.bugbattle.service;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import bugbattle.io.bugbattle.model.FeedbackModel;
import bugbattle.io.bugbattle.model.Interaction;
import bugbattle.io.bugbattle.model.Replay;
import bugbattle.io.bugbattle.util.ActivityUtil;
import bugbattle.io.bugbattle.util.ScreenshotUtil;

public class ReplaysDetector extends BBDetector {
    private Replay replay;
    private Handler handler;
    private boolean disabled = false;

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
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ViewGroup viewGroup = activity.findViewById(android.R.id.content);
                viewGroup.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        replay.addInteractionToCurrentScreenshot(new Interaction(Interaction.INTERACTION_TYPE.TOUCH, event.getX(), event.getY(), replay.getOffsetToDate()));
                        v.performClick();
                        return true;
                    }
                });
                handler.post(runnableCode);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    @Override
    public void resume() {
        disabled = false;
        handler.post(runnableCode);
    }

    @Override
    public void pause() {
        disabled = true;
        handler.removeCallbacks(runnableCode);
    }

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            if (!disabled) {
                Bitmap bitmap = ScreenshotUtil.takeScreenshot(0.4f);
                Activity activity = ActivityUtil.getCurrentActivity();
                if (activity != null) {
                    replay.setCurrentActivity(ActivityUtil.getCurrentActivity().getClass().getSimpleName());
                    if (bitmap != null) {
                        replay.addScreenshot(bitmap);
                    }
                }
                handler.postDelayed(this, replay.getInterval());
            }
        }
    };
}

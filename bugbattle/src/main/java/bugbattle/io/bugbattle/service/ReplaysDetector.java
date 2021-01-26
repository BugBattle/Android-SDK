package bugbattle.io.bugbattle.service;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;

import bugbattle.io.bugbattle.model.FeedbackModel;
import bugbattle.io.bugbattle.model.INTERACTIONTYPE;
import bugbattle.io.bugbattle.model.Interaction;
import bugbattle.io.bugbattle.model.Replay;
import bugbattle.io.bugbattle.util.ActivityUtil;
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
            Activity activity = ActivityUtil.getCurrentActivity();
            Bitmap bitmap = ScreenshotUtil.takeScreenshot(0.4f);
            if (bitmap != null) {
                String screenName = "MainActivity";
                if (activity != null) {
                    ViewGroup viewGroup = (ViewGroup) ((ViewGroup) activity
                            .findViewById(android.R.id.content)).getChildAt(0);
                    viewGroup.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                replay.addInteractionToCurrentReplay(new Interaction(event.getX(), event.getY(), new Date(), INTERACTIONTYPE.TD));
                            }
                            // if(event.getAction() == MotionEvent.ACTION_MOVE) {
                            //    replay.addInteractionToCurrentReplay(new Interaction(event.getX(), event.getY(), new Date(), INTERACTIONTYPE.TM));
                            //}
                            System.out.println(event.getAction());
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                replay.addInteractionToCurrentReplay(new Interaction(event.getX(), event.getY(), new Date(), INTERACTIONTYPE.TU));
                            }
                            return true;
                        }
                    });
                    screenName = activity.getClass().getSimpleName();
                }
                replay.addScreenshot(bitmap, screenName);
            }
            handler.postDelayed(this, replay.getInterval());
        }
    };
}

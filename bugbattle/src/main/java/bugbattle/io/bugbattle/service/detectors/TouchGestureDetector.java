package bugbattle.io.bugbattle.service.detectors;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import bugbattle.io.bugbattle.service.BBDetector;

public class TouchGestureDetector extends BBDetector {
    private static final int NUMBER_OF_FINGERS = 3;
    private boolean isDisabled = false;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;
    private long lastClickTime = 0;
    private Activity activity;

    public TouchGestureDetector(Application application) {
        super(application);
    }

    public TouchGestureDetector(Application application, Activity activity) {
        super(application);
        this.activity = activity;
    }

    @Override
    public void initialize() {
        if (this.activity != null) {
            View relativeLayout = this.activity.getWindow().getDecorView().getRootView();
            relativeLayout.setClickable(true);
            relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (!isDisabled) {
                        int action = event.getAction();
                        switch (action & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_POINTER_UP:
                                int count = event.getPointerCount();
                                if (count >= NUMBER_OF_FINGERS) {
                                    long clickTime = System.currentTimeMillis();
                                    if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                                        pause();
                                        takeScreenshot();
                                        lastClickTime = 0;
                                        return true;
                                    }
                                    lastClickTime = clickTime;
                                }
                                break;
                        }
                    }
                    return true;
                }
            });
        }
        /**
         * Attach listener to each new activity
         */
       application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            private View.OnClickListener onClickListener = null;

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                View relativeLayout = activity.getWindow().getDecorView().getRootView();
                relativeLayout.setClickable(true);
                relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (!isDisabled) {
                            int action = event.getAction();
                            switch (action & MotionEvent.ACTION_MASK) {
                                case MotionEvent.ACTION_POINTER_UP:
                                    int count = event.getPointerCount();
                                    if (count >= NUMBER_OF_FINGERS) {
                                        long clickTime = System.currentTimeMillis();
                                        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                                            pause();
                                            takeScreenshot();
                                            lastClickTime = 0;
                                            return true;
                                        }
                                        lastClickTime = clickTime;
                                    }
                                    break;
                            }

                        }
                        return true;
                        //do some stuff here
                    }
                });
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
                View relativeLayout = activity.getWindow().getDecorView().getRootView();
                relativeLayout.setClickable(false);
                relativeLayout.setOnClickListener(null);
            }
        });
    }

    @Override
    public void resume() {
        isDisabled = false;
    }

    @Override
    public void pause() {
        isDisabled = true;
    }
}

package gleap.io.gleap;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

class TouchGestureDetector extends GleapDetector {
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
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (!isDisabled) {
                        int action = motionEvent.getAction();
                        if ((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP) {
                            int count = motionEvent.getPointerCount();
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
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                View relativeLayout = activity.getWindow().getDecorView().getRootView();
                relativeLayout.setClickable(true);
                relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (!isDisabled) {
                            int action = motionEvent.getAction();
                            if ((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP) {
                                int count = motionEvent.getPointerCount();
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
                            }
                        }
                        return true;
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

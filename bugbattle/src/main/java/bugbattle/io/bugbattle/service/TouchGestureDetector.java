package bugbattle.io.bugbattle.service;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class TouchGestureDetector extends BBDetector {
    private static final int NUMBER_OF_FINGERS = 3;
    private boolean isDisabled = false;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;
    private long lastClickTime = 0;

    public TouchGestureDetector(Application application) {
        super(application);
    }


    @Override
    public void initialize() {

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            private View.OnClickListener onClickListener = null;

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                View relativeLayout = (View) activity.getWindow().getDecorView().getRootView();
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
                System.out.println("END");
                View relativeLayout = (View) activity.getWindow().getDecorView().getRootView();
                relativeLayout.setClickable(false);
                relativeLayout.setOnClickListener(null);
            }
        });
    }

    private void setChildListener(View parent, View.OnClickListener listener) {
        parent.setOnClickListener(listener);
        if (!(parent instanceof ViewGroup)) {
            return;
        }

        ViewGroup parentGroup = (ViewGroup) parent;
        for (int i = 0; i < parentGroup.getChildCount(); i++) {
            setChildListener(parentGroup.getChildAt(i), listener);
        }
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

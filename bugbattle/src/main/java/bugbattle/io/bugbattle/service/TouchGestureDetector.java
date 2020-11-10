package bugbattle.io.bugbattle.service;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class TouchGestureDetector extends BBDetector {
    private static final int NUMBER_OF_FINGERS = 3;
    private boolean isDisabled = false;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;
    private long lastClickTime = 0;

    public TouchGestureDetector(Activity activity) {
        super(activity);
    }


    @Override
    public void initialize() {
        FrameLayout fullScreenView = (FrameLayout) this.activity.getWindow().getDecorView();
        fullScreenView.setOnTouchListener(new BBListener());
    }

    @Override
    public void resume() {
        isDisabled = false;
    }

    @Override
    public void pause() {
        isDisabled = true;
    }

    private class BBListener implements View.OnTouchListener {
        @Override
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
                            }
                            lastClickTime = clickTime;
                        }
                        break;
                }
            }
            return false;
        }
    }
}




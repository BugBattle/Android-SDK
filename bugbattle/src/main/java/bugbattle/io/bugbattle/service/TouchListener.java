package bugbattle.io.bugbattle.service;

import android.view.MotionEvent;
import android.view.View;

public class TouchListener implements View.OnTouchListener {

    private View.OnTouchListener onTouchListener;

    TouchListener(View.OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (onTouchListener != null) {
            return onTouchListener.onTouch(v, event);
        }
        return false;
    }
}

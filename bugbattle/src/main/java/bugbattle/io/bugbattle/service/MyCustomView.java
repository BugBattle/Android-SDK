package bugbattle.io.bugbattle.service;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

public class MyCustomView extends View implements View.OnTouchListener {
    public MyCustomView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        System.out.println(event);
        return false;
    }
}

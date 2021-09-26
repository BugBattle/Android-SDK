package gleap.io.gleap;

import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;

/**
 * Enriched View with dimensions and type. To take a correct screenshot
 */
class ViewMeta {
    final View view;
    private final Rect frame;
    private final WindowManager.LayoutParams layoutParams;

    public ViewMeta(View view, Rect frame, WindowManager.LayoutParams layoutParams) {
        this.view = view;
        this.frame = frame;
        this.layoutParams = layoutParams;
    }

    public View getView() {
        return view;
    }

    public Rect getFrame() {
        return frame;
    }

    public WindowManager.LayoutParams getLayoutParams() {
        return layoutParams;
    }

    public boolean isDialogType() {
        return layoutParams.type == WindowManager.LayoutParams.TYPE_APPLICATION;
    }

    public boolean isActivityType() {
        return layoutParams.type == WindowManager.LayoutParams.TYPE_BASE_APPLICATION;
    }

    public IBinder getWindowToken() {
        return layoutParams.token;
    }

    public Context context() {
        return view.getContext();
    }
}

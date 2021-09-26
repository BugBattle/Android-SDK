package gleap.io.gleap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;


/**
 * Takes a screenshot of the current view
 */
class ScreenshotTaker {
    private final GleapBug gleapBug;

    public ScreenshotTaker() {
        gleapBug = GleapBug.getInstance();
    }

    /**
     * Take a screenshot of the current view and opens it in the editor
     */
    public void takeScreenshot() {
        GleapDetectorUtil.stopAllDetectors();
        if (GleapConfig.getInstance().getBugWillBeSentCallback() != null) {
            GleapConfig.getInstance().getBugWillBeSentCallback().flowInvoced();
        }
        Bitmap bitmap = ScreenshotUtil.takeScreenshot();
        if (bitmap != null) {
            openScreenshot(bitmap);
        }
    }

    public void openScreenshot(Bitmap imageFile) {
        try {
            Activity activity = ActivityUtil.getCurrentActivity();
            if (activity != null) {
                Context applicationContext = activity.getApplicationContext();
                if (applicationContext != null) {
                    if (GleapBug.getInstance().getPhoneMeta() != null) {
                        GleapBug.getInstance().getPhoneMeta().setLastScreen(applicationContext.getClass().getSimpleName());
                    }
                    SharedPreferences pref = applicationContext.getSharedPreferences("prefs", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("descriptionEditText", ""); // Storing the description
                    editor.apply();
                    Intent intent = new Intent(ActivityUtil.getCurrentActivity(), GleapMainActivity.class);
                    gleapBug.setScreenshot(imageFile);
                    activity.startActivity(intent);
                }
            }
        } catch (Exception ex) {

        }
    }
}

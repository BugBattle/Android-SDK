package bugbattle.io.bugbattle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import static bugbattle.io.bugbattle.ActivityUtil.getCurrentActivity;


/**
 * Takes a screenshot of the current view
 */
class ScreenshotTaker {
    private final BugBattleBug bugBattleBug;

    public ScreenshotTaker() {
        bugBattleBug = BugBattleBug.getInstance();
    }

    /**
     * Take a screenshot of the current view and opens it in the editor
     */
    public void takeScreenshot() {
        BBDetectorUtil.stopAllDetectors();
        if (BugBattleConfig.getInstance().getBugWillBeSentCallback() != null) {
            BugBattleConfig.getInstance().getBugWillBeSentCallback().flowInvoced();
        }
        Bitmap bitmap = ScreenshotUtil.takeScreenshot();
        if (bitmap != null) {
            openScreenshot(bitmap);
        }
    }

    public void openScreenshot(Bitmap imageFile) {
        if (getCurrentActivity() != null) {
            Context applicationContext = getCurrentActivity().getApplicationContext();
            if (applicationContext != null) {
                BugBattleBug.getInstance().getPhoneMeta().setLastScreen(applicationContext.getClass().getSimpleName());
                SharedPreferences pref = applicationContext.getSharedPreferences("prefs", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("descriptionEditText", ""); // Storing the description
                editor.apply();
                Intent intent = new Intent(getCurrentActivity(), BBMainActivity.class);
                bugBattleBug.setScreenshot(imageFile);
                getCurrentActivity().startActivity(intent);
                getCurrentActivity().overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
            }
        }
    }
}

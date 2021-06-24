package bugbattle.io.bugbattle.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import bugbattle.io.bugbattle.R;
import bugbattle.io.bugbattle.model.BugBattleBug;
import bugbattle.io.bugbattle.model.BugBattleConfig;
import bugbattle.io.bugbattle.util.BBDetectorUtil;
import bugbattle.io.bugbattle.util.ScreenshotUtil;
import bugbattle.io.bugbattle.view.BBMainActivity;

import static bugbattle.io.bugbattle.util.ActivityUtil.getCurrentActivity;


/**
 * Takes a screenshot of the current view
 */
public class ScreenshotTaker {
    private BugBattleBug bugBattleBug;

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
        openScreenshot(bitmap);
    }

    public void openScreenshot(Bitmap imageFile) {
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

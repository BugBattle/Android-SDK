package bugbattle.io.bugbattle.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import bugbattle.io.bugbattle.R;
import bugbattle.io.bugbattle.util.BBDetectorUtil;
import bugbattle.io.bugbattle.model.FeedbackModel;
import bugbattle.io.bugbattle.util.ScreenshotUtil;
import bugbattle.io.bugbattle.view.ImageEditor;

import static bugbattle.io.bugbattle.util.ActivityUtil.getCurrentActivity;


/**
 * Takes a screenshot of the current view
 */
public class ScreenshotTaker {
    private FeedbackModel feedbackModel;

    public ScreenshotTaker() {
        feedbackModel = FeedbackModel.getInstance();
    }

    /**
     * Take a screenshot of the current view and opens it in the editor
     */
    public void takeScreenshot() {
        BBDetectorUtil.stopAllDetectors();
        Bitmap bitmap = ScreenshotUtil.takeScreenshot();
        openScreenshot(bitmap);
    }

    public void openScreenshot(Bitmap imageFile) {
        SharedPreferences pref = getCurrentActivity().getApplicationContext().getSharedPreferences("prefs", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("descriptionEditText", ""); // Storing the description
        editor.apply();
        Intent intent = new Intent(getCurrentActivity(), ImageEditor.class);
        feedbackModel.setScreenshot(imageFile);
        getCurrentActivity().startActivity(intent);
        getCurrentActivity().overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
    }
}

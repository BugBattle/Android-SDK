package bugbattle.io.bugbattle.service;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.View;

import bugbattle.io.bugbattle.R;
import bugbattle.io.bugbattle.model.FeedbackModel;
import bugbattle.io.bugbattle.view.ImageEditor;


/**
 * Takes a screenshot of the current view
 */
public class ScreenshotTaker {
    private FeedbackModel feedbackModel;
    private Activity activity;

    public ScreenshotTaker(Activity activity) {
        feedbackModel = FeedbackModel.getInstance();
        this.activity = activity;
    }

    /**
     * Take a screenshot of the current view and opens it in the editor
     */
    public void takeScreenshot() {
        View v1 = activity.getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = v1.getDrawingCache();
        openScreenshot(bitmap);
        v1.setDrawingCacheEnabled(false);
    }

    public void openScreenshot(Bitmap imageFile) {
        SharedPreferences pref = this.activity.getApplicationContext().getSharedPreferences("prefs", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("descriptionEditText", ""); // Storing the description
        editor.apply();
        Intent intent = new Intent(activity, ImageEditor.class);
        Bitmap bitmap = getResizedBitmap(imageFile);
        feedbackModel.setScreenshot(bitmap);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
    }

    private Bitmap getResizedBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        int orientation = this.activity.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            matrix.postScale(0.7f, 0.7f);
        } else {
            matrix.postScale(0.5f, 0.5f);
        }

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
    }


}

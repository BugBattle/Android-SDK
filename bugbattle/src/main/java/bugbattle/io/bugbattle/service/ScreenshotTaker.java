package bugbattle.io.bugbattle.service;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.View;

import java.lang.reflect.Field;
import java.util.Map;

import bugbattle.io.bugbattle.view.ImageEditor;
import bugbattle.io.bugbattle.model.FeedbackModel;


/**
 * Takes a screenshot of the current view
 */
public class ScreenshotTaker {
    private FeedbackModel feedbackModel;

    public ScreenshotTaker() {
        feedbackModel = FeedbackModel.getInstance();

    }

    private static Activity getActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);

            Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
            if (activities == null)
                return null;

            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Take a screenshot of the current view and opens it in the editor
     */
    public void takeScreenshot() {
        View v1 = getActivity().getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = v1.getDrawingCache();
        bitmap = getResizedBitmap(bitmap);
        openScreenshot(bitmap);
        v1.setDrawingCacheEnabled(false);
    }

    private void openScreenshot(Bitmap imageFile) {
        Intent intent = new Intent(getActivity(), ImageEditor.class);
        feedbackModel.setScreenshot(imageFile);
        getActivity().startActivity(intent);
    }

    private Bitmap getResizedBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        int orientation = getActivity().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            matrix.postScale(0.7f, 0.7f);
        } else {
            matrix.postScale(0.5f, 0.5f);
        }

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


}

package bugbattle.io.bugbattle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Date;

 class ScreenshotTaker {
    private Activity activity;
    public ScreenshotTaker(Activity currActivity) {
        activity = currActivity;
        service = FeedbackService.getInstance();

    }
    private FeedbackService service;

     public static Bitmap loadBitmapFromView(Context context, View v) {
         DisplayMetrics dm = context.getResources().getDisplayMetrics();
         v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                 View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY));
         v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
         Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                 v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
         Canvas c = new Canvas(returnedBitmap);
         v.draw(c);

         return returnedBitmap;
     }

    public void takeScreenshot() throws Exception {
        // create bitmap screen capture
        View v1 = activity.getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = loadBitmapFromView(activity, v1);

        bitmap = getResizedBitmap(bitmap);
        openScreenshot(bitmap);
        bitmap = null;
        v1.setDrawingCacheEnabled(false);
    }
    public void openScreenshot(Bitmap imageFile) {
        Intent intent = new Intent(service.getMainActivity(), ImageEditor.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        service.setImage(imageFile);
        service.getMainActivity().startActivity(intent);
    }

    private Bitmap getResizedBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        int orientation = service.getMainActivity().getResources().getConfiguration().orientation;
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

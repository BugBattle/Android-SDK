package bugbattle.io.bugbattle.helper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import bugbattle.io.bugbattle.ImageEditor;

public class ScreenshotTaker {
    private Activity activity;
    public ScreenshotTaker(Activity currActivity) {
        activity = currActivity;
        service = FeedbackService.getInstance();
    }
    private FeedbackService service;

    public Bitmap takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyyMMddhhmmss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = activity.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            bitmap = getResizedBitmap(bitmap);
          //  File imageFile = new File(mPath);

            //FileOutputStream outputStream = new FileOutputStream(imageFile);
         //   int quality = 100;
    //            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);


         //   outputStream.flush();
          //  outputStream.close();

            openScreenshot(bitmap);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
        return null;
    }
    public void openScreenshot(Bitmap imageFile) {
        Intent intent = new Intent(activity, ImageEditor.class);
      //  Uri uri = Uri.fromFile(imageFile);
        service.setImage(imageFile);
        activity.startActivity(intent);
    }

    private Bitmap getResizedBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(0.7f, 0.7f);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }



}
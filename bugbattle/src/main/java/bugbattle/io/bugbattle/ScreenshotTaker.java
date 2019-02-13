package bugbattle.io.bugbattle;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

class ScreenshotTaker {
   // private Activity activity;
    public ScreenshotTaker(Activity currActivity) {
  //      activity = currActivity;
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

     public static Bitmap takeScreenShot(Activity activity) {
         View view = activity.getWindow().getDecorView();
         view.setDrawingCacheEnabled(true);
         view.buildDrawingCache();
         Bitmap b1 = view.getDrawingCache();
         Rect frame = new Rect();
         activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
         int statusBarHeight = frame.top;

         //Find the screen dimensions to create bitmap in the same size.
         int width = activity.getWindowManager().getDefaultDisplay().getWidth();
         int height = activity.getWindowManager().getDefaultDisplay().getHeight();

         Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
         view.destroyDrawingCache();
         return b;
     }


     public static Activity getActivity() {
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

             return null;
         }catch (Exception e) {


         }
         return null;
     }

     public void takeScreenshot() throws Exception {
     /*    ActivityManager am = (ActivityManager)activity.getSystemService(Context.ACTIVITY_SERVICE);
         ActivityManager.RunningTaskInfo cn = am.getRunningTasks(1).get(0);
         */


        // create bitmap screen capture
        View v1 = getActivity().getWindow().getDecorView().getRootView() ;
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = v1.getDrawingCache();


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

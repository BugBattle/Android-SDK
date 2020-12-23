package bugbattle.io.bugbattle.service;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import bugbattle.io.bugbattle.R;
import bugbattle.io.bugbattle.model.FeedbackModel;
import bugbattle.io.bugbattle.model.ViewMeta;
import bugbattle.io.bugbattle.view.ImageEditor;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND;


/**
 * Takes a screenshot of the current view
 */
public class ScreenshotTaker {
    private FeedbackModel feedbackModel;

    public ScreenshotTaker() {
        feedbackModel = FeedbackModel.getInstance();
    }

    private static Activity getCurrentActivity() {
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
        Bitmap bitmap = generateBitmap(getCurrentActivity());
        openScreenshot(bitmap);
    }

    public Bitmap generateBitmap(Activity activity) {
        final List<ViewMeta> viewRoots = getAvailableViewsEnriched(activity);
        int maxWidth = Integer.MIN_VALUE;
        int maxHeight = Integer.MIN_VALUE;
        for (ViewMeta viewMeta : viewRoots) {
            maxWidth = Math.max(viewMeta.getFrame().right, maxWidth);
            maxHeight = Math.max(viewMeta.getFrame().bottom, maxHeight);
        }
        final Bitmap bitmap = Bitmap.createBitmap(maxWidth, maxHeight, ARGB_8888);
        for (ViewMeta rootData : viewRoots) {
            if ((rootData.getLayoutParams().flags & FLAG_DIM_BEHIND) == FLAG_DIM_BEHIND) {
                Canvas dimCanvas = new Canvas(bitmap);
                int alpha = (int) (255 * rootData.getLayoutParams().dimAmount);
                dimCanvas.drawARGB(alpha, 0, 0, 0);
            }
            Canvas canvas = new Canvas(bitmap);
            canvas.translate(rootData.getFrame().left, rootData.getFrame().top);
            rootData.getView().draw(canvas);
        }
        return bitmap;
    }


    public void openScreenshot(Bitmap imageFile) {
        SharedPreferences pref = getCurrentActivity().getApplicationContext().getSharedPreferences("prefs", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("descriptionEditText", ""); // Storing the description
        editor.apply();
        Intent intent = new Intent(getCurrentActivity(), ImageEditor.class);
        Bitmap bitmap = getResizedBitmap(imageFile);
        feedbackModel.setScreenshot(bitmap);
        getCurrentActivity().startActivity(intent);
        getCurrentActivity().overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
    }

    private void getOffset(List<ViewMeta> rootViews) {
        int minTop = Integer.MAX_VALUE;
        int minLeft = Integer.MAX_VALUE;
        for (ViewMeta rootView : rootViews) {
            if (rootView.getFrame().top < minTop) {
                minTop = rootView.getFrame().top;
            }
            if (rootView.getFrame().left < minLeft) {
                minLeft = rootView.getFrame().left;
            }
        }
        for (ViewMeta rootView : rootViews) {
            rootView.getFrame().offset(-minLeft, -minTop);
        }
    }

    private Bitmap getResizedBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        Matrix matrix = new Matrix();
        int orientation = getCurrentActivity().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            matrix.postScale(0.7f, 0.7f);
        } else {
            matrix.postScale(0.5f, 0.5f);
        }
        return Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
    }

    private Field getFieldForName(String name, Object obj) throws NullPointerException {
        Class currentClass = obj.getClass();
        while (currentClass != Object.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (name.equals(field.getName())) {
                    return field;
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        throw new NullPointerException();
    }

    private Object getField(String fieldName, Object target) {
        try {
            Field field = getFieldForName(fieldName, target);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<ViewMeta> getAvailableViewsEnriched(Activity activity) {
        Object globalWindowManager = getField("mGlobal", activity.getWindowManager());
        Object rootObjects = getField("mRoots", globalWindowManager);
        Object paramsObject = getField("mParams", globalWindowManager);
        Object[] roots = ((List) rootObjects).toArray();
        List<WindowManager.LayoutParams> paramsList = (List<WindowManager.LayoutParams>) paramsObject;
        WindowManager.LayoutParams[] params = paramsList.toArray(new WindowManager.LayoutParams[paramsList.size()]);
        List<ViewMeta> rootViews = enrichViewsWithMeta(roots, params);
        if (rootViews.isEmpty()) {
            return Collections.emptyList();
        }
        getOffset(rootViews);
        reArrangeViews(rootViews);
        return rootViews;
    }

    private void reArrangeViews(List<ViewMeta> metaViews) {
        if (metaViews.size() <= 1) {
            return;
        }
        for (int dialogIndex = 0; dialogIndex < metaViews.size() - 1; dialogIndex++) {
            ViewMeta viewRoot = metaViews.get(dialogIndex);
            if (!viewRoot.isDialogType()) {
                continue;
            }
            if (viewRoot.getWindowToken() == null) {
                return;
            }
            for (int parentIndex = dialogIndex + 1; parentIndex < metaViews.size(); parentIndex++) {
                ViewMeta possibleParent = metaViews.get(parentIndex);
                if (possibleParent.isActivityType()
                        && possibleParent.getWindowToken() == viewRoot.getWindowToken()) {
                    metaViews.remove(possibleParent);
                    metaViews.add(dialogIndex, possibleParent);
                    break;
                }
            }
        }
    }

    private List<ViewMeta> enrichViewsWithMeta(Object[] roots, WindowManager.LayoutParams[] params) {
        List<ViewMeta> metaViews = new ArrayList<>();
        int currIndex = 0;
        for (Object view : roots) {
            View rootView = (View) getField("mView", view);
            if (rootView == null) {
                currIndex++;
                continue;
            }
            if (!rootView.isShown()) {
                currIndex++;
                continue;
            }
            int[] xyDimension = new int[2];
            rootView.getLocationOnScreen(xyDimension);
            int left = xyDimension[0];
            int top = xyDimension[1];
            Rect rect = new Rect(left, top, left + rootView.getWidth(), top + rootView.getHeight());
            metaViews.add(new ViewMeta(rootView, rect, params[currIndex]));
            currIndex++;
        }
        return metaViews;
    }
}

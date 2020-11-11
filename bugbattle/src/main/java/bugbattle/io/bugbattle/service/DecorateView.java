package bugbattle.io.bugbattle.service;

import android.view.Window;

import java.lang.reflect.Field;

public class DecorateView {
    private static Class classDecorView;
    private static Class classPopupDecorView;

    static {
        try {
            classDecorView = Class.forName("com.android.internal.policy.DecorView");
            classPopupDecorView = Class.forName("android.widget.PopupWindow$PopupDecorView");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void useWindowCallbackWrapper(Object decorViewObject) throws NoSuchFieldException, IllegalAccessException {

        Field mWindowField = classDecorView.getDeclaredField("mWindow");

        mWindowField.setAccessible(true);

        Window mWindowObject = (Window) mWindowField.get(decorViewObject);

        Window.Callback callback = mWindowObject.getCallback();

        Window.Callback windowCallbackWrapper = new CallbackWrapper(callback);

        mWindowObject.setCallback(windowCallbackWrapper);

    }


    static void infiltrateFor(Object objectDecorView) {

        try {
            if (classDecorView.isInstance(objectDecorView)) {

                useWindowCallbackWrapper(objectDecorView);

            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

}

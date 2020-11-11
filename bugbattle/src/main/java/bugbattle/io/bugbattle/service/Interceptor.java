package bugbattle.io.bugbattle.service;

import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public class Interceptor {
    public static void infiltrate() {
        try {
            Class classWindowManagerGlobal = Class.forName("android.view.WindowManagerGlobal");

            Field mViewsField = classWindowManagerGlobal.getDeclaredField("mViews");

            makeFieldNonFinal(mViewsField);

            Method getInstanceMethod = classWindowManagerGlobal.getDeclaredMethod("getInstance");

            Object windowManagerGlobalObject = getInstanceMethod.invoke(null);

            List<View> mViews = new InterceptorList();

            mViewsField.set(windowManagerGlobalObject, mViews);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void makeFieldNonFinal(Field targetField) {

        try {

            Class classField = Field.class;

            Field accessFlagsField = classField.getDeclaredField("accessFlags");

            accessFlagsField.setAccessible(true);

            targetField.setAccessible(true);

            accessFlagsField.setInt(targetField, targetField.getModifiers() & ~Modifier.FINAL);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}

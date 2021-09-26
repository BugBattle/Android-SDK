package gleap.io.gleap;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

class GleapDetectorUtil {

    public static void resumeAllDetectors() {
        for (GleapDetector detector : GleapConfig.getInstance().getGestureDetectors()) {
            detector.resume();
        }
    }

    public static void stopAllDetectors() {
        for (GleapDetector detector : GleapConfig.getInstance().getGestureDetectors()) {
            detector.pause();
        }
    }

    public static List<GleapDetector> initDetectors(Application application, Activity activity, GleapActivationMethod[] activationMethods) {
        List<GleapDetector> detectorList = new LinkedList<>();
        for (GleapActivationMethod activationMethod : activationMethods) {
            if (activationMethod == GleapActivationMethod.SHAKE) {
                GleapDetector detector = new ShakeGestureDetector(application);
                detector.initialize();
                detectorList.add(detector);
            }
            if (activationMethod == GleapActivationMethod.THREE_FINGER_DOUBLE_TAB) {
                TouchGestureDetector touchGestureDetector;
                if (activity != null) {
                    touchGestureDetector = new TouchGestureDetector(application, activity);
                } else {
                    touchGestureDetector = new TouchGestureDetector(application);
                }
                touchGestureDetector.initialize();
                detectorList.add(touchGestureDetector);
            }
            if (activationMethod == GleapActivationMethod.SCREENSHOT) {
                ScreenshotGestureDetector screenshotGestureDetector;
                screenshotGestureDetector = new ScreenshotGestureDetector(application);
                screenshotGestureDetector.initialize();
                detectorList.add(screenshotGestureDetector);
            }
        }
        return detectorList;
    }
}

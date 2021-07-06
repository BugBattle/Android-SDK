package bugbattle.io.bugbattle;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

class BBDetectorUtil {

    public static void resumeAllDetectors() {
        for (BBDetector detector : BugBattleConfig.getInstance().getGestureDetectors()) {
            detector.resume();
        }
    }

    public static void stopAllDetectors() {
        for (BBDetector detector : BugBattleConfig.getInstance().getGestureDetectors()) {
            detector.pause();
        }
    }

    public static List<BBDetector> initDetectors(Application application, Activity activity, BugBattleActivationMethod[] activationMethods) {
        List<BBDetector> detectorList = new LinkedList<>();
        for (BugBattleActivationMethod activationMethod : activationMethods) {
            if (activationMethod == BugBattleActivationMethod.SHAKE) {
                BBDetector detector = new ShakeGestureDetector(application);
                detector.initialize();
                detectorList.add(detector);
            }
            if (activationMethod == BugBattleActivationMethod.THREE_FINGER_DOUBLE_TAB) {
                TouchGestureDetector touchGestureDetector;
                if (activity != null) {
                    touchGestureDetector = new TouchGestureDetector(application, activity);
                } else {
                    touchGestureDetector = new TouchGestureDetector(application);
                }
                touchGestureDetector.initialize();
                detectorList.add(touchGestureDetector);
            }
            if (activationMethod == BugBattleActivationMethod.SCREENSHOT) {
                ScreenshotGestureDetector screenshotGestureDetector;
                screenshotGestureDetector = new ScreenshotGestureDetector(application);
                screenshotGestureDetector.initialize();
                detectorList.add(screenshotGestureDetector);
            }
        }
        return detectorList;
    }
}

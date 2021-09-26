package gleap.io.gleap;

import android.app.Application;

class ReplayUtil {
    public static void enableReplay(boolean enable, Application application) {
        if (enable) {
            ReplaysDetector replaysDetector = new ReplaysDetector(application);
            replaysDetector.initialize();
            GleapConfig.getInstance().getGestureDetectors().add(replaysDetector);
        }
    }
}

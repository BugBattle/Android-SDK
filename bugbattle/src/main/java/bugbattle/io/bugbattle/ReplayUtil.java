package bugbattle.io.bugbattle;

import android.app.Application;

class ReplayUtil {
    public static void enableReplay(boolean enable, Application application) {
        if (enable) {
            ReplaysDetector replaysDetector = new ReplaysDetector(application);
            replaysDetector.initialize();
            BugBattleConfig.getInstance().getGestureDetectors().add(replaysDetector);
        }
    }
}

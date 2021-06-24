package bugbattle.io.bugbattle.util;

import android.app.Application;

import bugbattle.io.bugbattle.model.BugBattleConfig;
import bugbattle.io.bugbattle.service.detectors.ReplaysDetector;

public class ReplayUtil {
    public static void enableReplay(boolean enable, Application application) {
        if (enable) {
            ReplaysDetector replaysDetector = new ReplaysDetector(application);
            replaysDetector.initialize();
            BugBattleConfig.getInstance().getGestureDetectors().add(replaysDetector);
        }
    }
}

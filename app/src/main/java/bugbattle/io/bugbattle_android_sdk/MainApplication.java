package bugbattle.io.bugbattle_android_sdk;

import android.app.Application;

import bugbattle.io.bugbattle.BugBattle;
import bugbattle.io.bugbattle.controller.BugBattleActivationMethod;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BugBattle.initialise("v3l957lDVxJkVBZjlNx4L7KAMoWrnndi", BugBattleActivationMethod.THREE_FINGER_DOUBLE_TAB, this);
    }
}

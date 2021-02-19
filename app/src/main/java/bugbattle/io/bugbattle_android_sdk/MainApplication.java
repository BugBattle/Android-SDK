package bugbattle.io.bugbattle_android_sdk;

import android.app.Application;

import bugbattle.io.bugbattle.BugBattle;
import bugbattle.io.bugbattle.controller.BugBattleActivationMethod;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BugBattleActivationMethod[] detectorList = {BugBattleActivationMethod.SHAKE, BugBattleActivationMethod.SCREENSHOT, BugBattleActivationMethod.THREE_FINGER_DOUBLE_TAB};

        BugBattle.initialise("YOUR_APK_KEY", detectorList, this);
        BugBattle.setLanguage("deee");
        BugBattle.setPrivacyPolicyUrl("TESTURL.com");
        BugBattle.enablePrivacyPolicy(true);
         BugBattle.enableReplay();
    }
}

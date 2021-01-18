package bugbattle.io.bugbattle_android_sdk;

import android.app.Application;

import bugbattle.io.bugbattle.BugBattle;
import bugbattle.io.bugbattle.controller.BugBattleActivationMethod;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BugBattleActivationMethod[] detectorList = {BugBattleActivationMethod.SHAKE, BugBattleActivationMethod.SCREENSHOT, BugBattleActivationMethod.THREE_FINGER_DOUBLE_TAB};
        BugBattle.setApiURL("https://93d5920f0035.ngrok.io");
        BugBattle.initialise("U1FeTUrxnzbtB8ebJj2unNweR6pzgIWg", detectorList, this);
    }
}

package bugbattle.io.bugbattle;

import android.app.Activity;

import org.json.JSONException;

import java.io.IOException;

import bugbattle.io.bugbattle.helper.FeedbackService;
import bugbattle.io.bugbattle.helper.ShakeGestureDetector;
import bugbattle.io.bugbattle.helper.StepsToReproduce;

public class BugBattle {
    private static BugBattle instance;
    private BugBattleActivationMethod bugBattleActivationMethod;
    private static ShakeGestureDetector shakeGestureDetector;
    private static StepsToReproduce stepsToReproduce;

    private BugBattle(String sdkKey, BugBattleActivationMethod activationMethod, Activity mainActivity) {
        try {
            Runtime.getRuntime().exec("logcat -c");
        } catch (IOException e) {
            System.out.println(e);
        }
        FeedbackService service = FeedbackService.init();
        stepsToReproduce = StepsToReproduce.getInstance();
        service.setSdkKey(sdkKey);
        bugBattleActivationMethod = activationMethod;
        shakeGestureDetector = new ShakeGestureDetector(mainActivity);



    }

    public static void initialize(String sdkKey, BugBattleActivationMethod activationMethod, Activity mainActivity) {
        if(instance == null){
            instance = new BugBattle(sdkKey, activationMethod, mainActivity);
        }
    }

    public static void trigger() {

    }

    public static void addStep(String type, String description) throws JSONException {
        stepsToReproduce.setStep(type, description);
    }

    public static void resume() {
        if(instance != null){
            shakeGestureDetector.resume();
        }
    }

    public static void pause() {
        if(instance != null) {
            shakeGestureDetector.pause();
        }
    }
}

package bugbattle.io.bugbattle;

import android.app.Activity;

import org.json.JSONException;

import java.io.IOException;


public class BugBattle {
    private static BugBattle instance;
    private BugBattleActivationMethod bugBattleActivationMethod;
    private static ShakeGestureDetector shakeGestureDetector;
    private static StepsToReproduce stepsToReproduce;
    private static FeedbackService service;
    private BugBattle(String sdkKey, BugBattleActivationMethod activationMethod, Activity mainActivity) {
        try {
            Runtime.getRuntime().exec("logcat -c");
        } catch (IOException e) {
            System.out.println(e);
        }
        service = FeedbackService.init();
        stepsToReproduce = StepsToReproduce.getInstance();
        service.setMainActivity(mainActivity);
        service.setSdkKey(sdkKey);
        bugBattleActivationMethod = activationMethod;
        shakeGestureDetector = new ShakeGestureDetector(mainActivity);
    }

    /**
     * Initialises the Bugbattle SDK.
     * @param mainActivity The main activity of your application
     * @param sdkKey The SDK key, which can be found on dashboard.bugbattle.io
     * @param activationMethod Activation method, which triggers a new bug report.
     */
    public static void initialise(Activity mainActivity, String sdkKey, BugBattleActivationMethod activationMethod) {
        if(instance == null){
            instance = new BugBattle(sdkKey, activationMethod, mainActivity);
        }
    }

    /**
     * Trigger the report flow manually
     */
    public static void trigger() {
        ScreenshotTaker sc = new ScreenshotTaker(service.getMainActivity());
        try {
            sc.takeScreenshot();
        }catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Track a step to add more information to the bug report
     * @param type
     * @param description
     * @throws JSONException
     */
    public static void trackStep(String type, String description) {
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

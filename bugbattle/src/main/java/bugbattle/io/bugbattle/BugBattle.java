package bugbattle.io.bugbattle;

import android.app.Application;

import org.json.JSONException;
import org.json.JSONObject;

import bugbattle.io.bugbattle.controller.BugBattleActivationMethod;
import bugbattle.io.bugbattle.controller.BugBattleNotInitialisedException;
import bugbattle.io.bugbattle.model.Feedback;
import bugbattle.io.bugbattle.model.STEPTYPE;
import bugbattle.io.bugbattle.controller.StepsToReproduce;
import bugbattle.io.bugbattle.service.ScreenshotTaker;
import bugbattle.io.bugbattle.service.ShakeGestureDetector;


public class BugBattle {
    private static BugBattle instance;
    private static ShakeGestureDetector shakeGestureDetector;
    private static StepsToReproduce stepsToReproduce;
    private static Feedback service;

    private BugBattle(String sdkKey, BugBattleActivationMethod activationMethod, Application application) {
        service = Feedback.init();
        stepsToReproduce = StepsToReproduce.getInstance();
        service.setContext(application.getApplicationContext());
        service.setSdkKey(sdkKey);
        if(activationMethod == BugBattleActivationMethod.SHAKE) {
            shakeGestureDetector = new ShakeGestureDetector(application.getApplicationContext());
            service.setShakeGestureDetector(shakeGestureDetector);
        }
    }

    /**
     * Initialises the Bugbattle SDK.
     * @param application The application (this)
     * @param sdkKey The SDK key, which can be found on dashboard.bugbattle.io
     * @param activationMethod Activation method, which triggers a new bug report.
     */
    public static BugBattle initialise(String sdkKey, BugBattleActivationMethod activationMethod,Application application) {
        if(instance == null){
            instance = new BugBattle(sdkKey, activationMethod, application);
        }
        return instance;
    }

    /**
     * Manually start the bug reporting workflow. This is used, when you use the activation method "NONE".
     * @throws BugBattleNotInitialisedException thrown when BugBattle is not initialised
     */
    public static void startBugReporting() throws BugBattleNotInitialisedException{
        if(instance != null) {
            ScreenshotTaker sc = new ScreenshotTaker();

            try {
                sc.takeScreenshot();
            }catch (Exception e) {
                System.out.println(e);
            }
        } else {
            throw new BugBattleNotInitialisedException("BugBattle is not initialised");
        }

    }



    /**
     * Track a step to add more information to the bug report
     * @param type Type of the step. (Use any custom string or one of the predefined constants {@link STEPTYPE})
     * @param data Custom data associated with the step.
     * @throws JSONException
     */
    public static void trackStep(String type, String data) {
        stepsToReproduce.setStep(type, data);
    }

    /**
     * Set a custom app bar color to fit the bug report more your app style.
     * @param color the background color of the app bar.
     */
    public static void setAppBarColor(String color) {
        service.setAppBarColor(color);
    }

    /**
     * Attach cusom data, which can be view in the BugBattle dashboard.
     * @param customData The data to attach to a bug report
     */
    public static void attachCustomData(JSONObject customData) {
        service.setCustomData(customData);
    }
}

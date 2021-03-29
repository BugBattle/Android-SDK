package bugbattle.io.bugbattle;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bugbattle.io.bugbattle.controller.BugBattleActivationMethod;
import bugbattle.io.bugbattle.controller.BugBattleNotInitialisedException;
import bugbattle.io.bugbattle.model.APPLICATIONTYPE;
import bugbattle.io.bugbattle.model.FeedbackModel;
import bugbattle.io.bugbattle.model.PhoneMeta;
import bugbattle.io.bugbattle.service.BBDetector;
import bugbattle.io.bugbattle.service.ReplaysDetector;
import bugbattle.io.bugbattle.service.ScreenshotGestureDetector;
import bugbattle.io.bugbattle.service.ScreenshotTaker;
import bugbattle.io.bugbattle.service.ShakeGestureDetector;
import bugbattle.io.bugbattle.service.TouchGestureDetector;

public class BugBattle {
    private static BugBattle instance;
    private static ScreenshotTaker screenshotTaker;
    private static Activity activity;
    private static Application application;

    private BugBattle(String sdkKey, BugBattleActivationMethod[] activationMethods, Application application) {
        BugBattle.application = application;
        FeedbackModel.getInstance().setSdkKey(sdkKey);
        FeedbackModel.getInstance().setPhoneMeta(new PhoneMeta(application.getApplicationContext()));
        screenshotTaker = new ScreenshotTaker();

        try {
            Runtime.getRuntime().exec("logcat - c");
        } catch (Exception e) {
            System.out.println(e);
        }
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
        FeedbackModel.getInstance().setGestureDetectors(detectorList);
    }

    /**
     * Initialises the Bugbattle SDK.
     *
     * @param application       The application (this)
     * @param sdkKey            The SDK key, which can be found on dashboard.bugbattle.io
     * @param activationMethods Activation method, which triggers a new bug report.
     * @param activity          main activity
     */
    public static BugBattle initialise(String sdkKey, final BugBattleActivationMethod[] activationMethods, Application application, Activity activity) {
        BugBattle.activity = activity;
        if (instance == null) {
            instance = new BugBattle(sdkKey, activationMethods, application);
        }
        return instance;
    }

    /**
     * Initialises the Bugbattle SDK.
     *
     * @param application       The application (this)
     * @param sdkKey            The SDK key, which can be found on dashboard.bugbattle.io
     * @param activationMethods Activation method, which triggers a new bug report.
     */
    public static BugBattle initialise(String sdkKey, final BugBattleActivationMethod[] activationMethods, Application application) {

        if (instance == null) {
            instance = new BugBattle(sdkKey, activationMethods, application);
        }
        return instance;
    }

    /**
     * Initialises the Bugbattle SDK.
     *
     * @param application      The application (this)
     * @param sdkKey           The SDK key, which can be found on dashboard.bugbattle.io
     * @param activationMethod Activation method, which triggers a new bug report.
     */
    public static BugBattle initialise(String sdkKey, final BugBattleActivationMethod activationMethod, Application application) {
        List<BugBattleActivationMethod> activationMethods = new ArrayList<>();
        activationMethods.add(activationMethod);
        if (instance == null) {
            instance = new BugBattle(sdkKey, activationMethods.toArray(new BugBattleActivationMethod[0]), application);
        }
        return instance;
    }

    /**
     * Manually start the bug reporting workflow. This is used, when you use the activation method "NONE".
     *
     * @throws BugBattleNotInitialisedException thrown when BugBattle is not initialised
     */
    public static void startBugReporting() throws BugBattleNotInitialisedException {
        if (instance != null) {
            try {
                screenshotTaker.takeScreenshot();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new BugBattleNotInitialisedException("BugBattle is not initialised");
        }
    }

    /**
     * Starts the bug reporting with a custom screenshot attached.
     *
     * @param bitmap the image will be used instead of the current
     */
    public static void startBugReporting(Bitmap bitmap) {
        FeedbackModel.getInstance().setScreenshot(bitmap);
        screenshotTaker.openScreenshot(bitmap);
    }

    /**
     * Attach cusom data, which can be view in the BugBattle dashboard.
     *
     * @param customData The data to attach to a bug report
     */
    public static void attachCustomData(JSONObject customData) {
        FeedbackModel.getInstance().setCustomData(customData);
    }

    /**
     * Set/Prefill the email address for the user.
     *
     * @param email address, which is fileld in.
     */
    public static void setCustomerEmail(String email) {
        FeedbackModel.getInstance().setEmail(email);
    }

    /**
     * Enables the privacy policy check.
     *
     * @param enable Enable the privacy policy.
     */
    public static void enablePrivacyPolicy(boolean enable) {
        FeedbackModel.getInstance().enablePrivacyPolicy(enable);
    }

    /**
     * Sets a custom privacy policy url.
     *
     * @param privacyUrl The URL pointing to your privacy policy.
     */
    public static void setPrivacyPolicyUrl(String privacyUrl) {
        FeedbackModel.getInstance().setPrivacyPolicyUrl(privacyUrl);
    }

    /**
     * Sets the API url to your internal Bugbattle server. Please make sure that the server is reachable within the network
     * If you use a http url pls add android:usesCleartextTraffic="true" to your main activity to allow cleartext traffic
     *
     * @param apiUrl url of the internal Bugbattle server
     */
    public static void setApiURL(String apiUrl) {
        FeedbackModel.getInstance().setApiUrl(apiUrl);
    }

    /**
     * This is called, when the bugbattle flow is started
     *
     * @param bugWillBeSentCallback is called when BB is opened
     */
    public static void setBugWillBeSentCallback(BugWillBeSentCallback bugWillBeSentCallback) {
        FeedbackModel.getInstance().setBugWillBeSentCallback(bugWillBeSentCallback);
    }

    /**
     * This method is triggered, when the bugbattle flow is closed
     *
     * @param bugSentCallback this callback is called when the flow is called
     */
    public static void setBugSentCallback(BugSentCallback bugSentCallback) {
        FeedbackModel.getInstance().setBugSentCallback(bugSentCallback);
    }


    /**
     * Customize the way, the Bitmap is generated. If this is overritten,
     * only the custom way is used
     *
     * @param getBitmapCallback get the Bitmap
     */
    public static void setBitmapCallback(GetBitmapCallback getBitmapCallback) {
        FeedbackModel.getInstance().setGetBitmapCallback(getBitmapCallback);
    }


    /**
     * Set Application Type
     *
     * @param applicationType "Android", "RN", "Flutter"
     */
    public static void setApplicationType(APPLICATIONTYPE applicationType) {
        FeedbackModel.getInstance().setApplicationtype(applicationType);
    }

    /**
     * Enable Replay function for BB
     * Use with care, check performance on phone
     */
    public static void enableReplay() {
        ReplaysDetector replaysDetector = new ReplaysDetector(application);
        replaysDetector.initialize();
        FeedbackModel.getInstance().getGestureDetectors().add(replaysDetector);
    }

    /**
     * Set the language for the BugBattle Report Flow. Otherwise the default language is used.
     * Supported Languages "en", "es", "fr", "it", "de"
     *
     * @param language ISO Country Code eg. "en", "de", "es"
     */
    public static void setLanguage(String language) {
        FeedbackModel.getInstance().setLanguage(language);
    }

    public static void attachData(JSONObject data) {
        FeedbackModel.getInstance().setData(data);
    }

}

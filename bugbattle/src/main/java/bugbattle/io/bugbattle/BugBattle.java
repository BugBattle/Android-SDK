package bugbattle.io.bugbattle;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

import org.json.JSONObject;

import bugbattle.io.bugbattle.controller.BugBattleActivationMethod;
import bugbattle.io.bugbattle.controller.BugBattleNotInitialisedException;
import bugbattle.io.bugbattle.model.APPLICATIONTYPE;
import bugbattle.io.bugbattle.model.FeedbackModel;
import bugbattle.io.bugbattle.model.PhoneMeta;
import bugbattle.io.bugbattle.service.BBDetector;
import bugbattle.io.bugbattle.service.ScreenshotGestureDetector;
import bugbattle.io.bugbattle.service.ScreenshotTaker;
import bugbattle.io.bugbattle.service.ShakeGestureDetector;
import bugbattle.io.bugbattle.service.TouchGestureDetector;

public class BugBattle {
    private static BugBattle instance;
    private static ScreenshotTaker screenshotTaker;
    private static Activity activity;

    private BugBattle(String sdkKey, BugBattleActivationMethod activationMethod, Application application) {

        FeedbackModel.getInstance().setSdkKey(sdkKey);
        FeedbackModel.getInstance().setPhoneMeta(new PhoneMeta(application.getApplicationContext()));
        screenshotTaker = new ScreenshotTaker();

        try {
            Runtime.getRuntime().exec("logcat - c");
        } catch (Exception e) {
            System.out.println(e);
        }

        if (activationMethod == BugBattleActivationMethod.SHAKE) {
            BBDetector detector = new ShakeGestureDetector(application);
            FeedbackModel.getInstance().setGestureDetector(detector);
            detector.initialize();
        }
        if (activationMethod == BugBattleActivationMethod.THREE_FINGER_DOUBLE_TAB) {
            TouchGestureDetector touchGestureDetector;
            if (activity != null) {
                touchGestureDetector = new TouchGestureDetector(application, activity);
            } else {
                touchGestureDetector = new TouchGestureDetector(application);
            }
            FeedbackModel.getInstance().setGestureDetector(touchGestureDetector);
            touchGestureDetector.initialize();
        }
        if (activationMethod == BugBattleActivationMethod.SCREENSHOT) {
            ScreenshotGestureDetector screenshotGestureDetector;
            if (activity != null) {
                screenshotGestureDetector = new ScreenshotGestureDetector(application, activity);
            } else {
                screenshotGestureDetector = new ScreenshotGestureDetector(application);
            }
            FeedbackModel.getInstance().setGestureDetector(screenshotGestureDetector);
            screenshotGestureDetector.initialize();
        }
    }

    /**
     * Initialises the Bugbattle SDK.
     *
     * @param application      The application (this)
     * @param sdkKey           The SDK key, which can be found on dashboard.bugbattle.io
     * @param activationMethod Activation method, which triggers a new bug report.
     */
    public static BugBattle initialise(String sdkKey, final BugBattleActivationMethod activationMethod, Application application) {

        if (instance == null) {
            instance = new BugBattle(sdkKey, activationMethod, application);
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
     * This method is triggered, when the bugbattle flow is closed
     *
     * @param closeCallback
     */
    public static void setCloseCallback(CloseCallback closeCallback) {
        FeedbackModel.getInstance().setCloseCallback(closeCallback);
    }

    /**
     * This is called, when the bugbattle flow is started
     *
     * @param flowInvoked
     */
    public static void setFlowInvoked(FlowInvoked flowInvoked) {
        FeedbackModel.getInstance().setFlowInvoked(flowInvoked);
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


    public static void setApplicationType(APPLICATIONTYPE applicationType) {
        FeedbackModel.getInstance().setApplicationtype(applicationType);
    }
}

package bugbattle.io.bugbattle;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Color;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BugBattle implements iBugBattle {
    private static BugBattle instance;
    private static ScreenshotTaker screenshotTaker;
    private static Activity activity;
    private static Application application;
    private static boolean useAutoconfig = false;

    private BugBattle() {
    }

    /**
     * Init bugbattle with the given properties
     */
    private static void initBugBattle(String sdkKey, BugBattleActivationMethod[] activationMethods, Application application) {
        try {
            //prepare bugbattle
            BugBattle.application = application;
            screenshotTaker = new ScreenshotTaker();
            ConsoleUtil.clearConsole();
            //init config and load from the server
            BugBattleConfig.getInstance().setSdkKey(sdkKey);

            if (useAutoconfig) {
                new BugBattleListener();
            }

            //init bugbattle bug
            BugBattleBug.getInstance().setPhoneMeta(new PhoneMeta(application.getApplicationContext()));

            BugBattle.getInstance().enableReplays(BugBattleConfig.getInstance().isEnableReplays());

            //start activation methods
            List<BBDetector> detectorList = BBDetectorUtil.initDetectors(application, activity, activationMethods);

            if (BugBattleConfig.getInstance().isEnableReplays()) {
                ReplaysDetector replaysDetector = new ReplaysDetector(application);
                replaysDetector.initialize();
                detectorList.add(replaysDetector);
            }
            BugBattleConfig.getInstance().setGestureDetectors(detectorList);
            BBDetectorUtil.resumeAllDetectors();
        } catch (Exception err) {
        }
    }

    /**
     * Get an instance of bugbattle
     *
     * @return instance of bugbattle
     */
    public static BugBattle getInstance() {
        if (instance == null) {
            instance = new BugBattle();
        }
        return instance;
    }

    /**
     * Auto-configures the BugBattle SDK from the remote config.
     *
     * @param sdkKey      The SDK key, which can be found on dashboard.bugbattle.io
     * @param application used to have context and access to take screenshot
     */
    public static void autoConfigure(String sdkKey, Application application) {
        useAutoconfig = true;
        BugBattle.application = application;
        BugBattleConfig.getInstance().setSdkKey(sdkKey);
        new BugBattleListener();
    }

    /**
     * Initialises the Bugbattle SDK.
     *
     * @param application       The application (this)
     * @param sdkKey            The SDK key, which can be found on dashboard.bugbattle.io
     * @param activationMethods Activation method, which triggers a new bug report.
     * @param activity          main activity
     */
    public static BugBattle initWithToken(String sdkKey, BugBattleActivationMethod[] activationMethods, Application application, Activity activity) {
        try {
            BugBattle.activity = activity;
            if (instance == null) {
                instance = new BugBattle();
            }
            initBugBattle(sdkKey, activationMethods, application);
        } catch (Exception exception) {
            exception.printStackTrace();
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
    public static BugBattle initWithToken(String sdkKey, BugBattleActivationMethod activationMethod, Application application) {
        try {
            List<BugBattleActivationMethod> activationMethods = new ArrayList<>();
            activationMethods.add(activationMethod);
            if (instance == null) {
                instance = new BugBattle();
            }
            initBugBattle(sdkKey, activationMethods.toArray(new BugBattleActivationMethod[0]), application);
        } catch (Exception exception) {
            exception.printStackTrace();
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
    @Deprecated
    public static BugBattle initialise(String sdkKey, BugBattleActivationMethod[] activationMethods, Application application) {
        return initWithToken(sdkKey, activationMethods, application, activity);
    }

    /**
     * Initialises the Bugbattle SDK.
     *
     * @param application      The application (this)
     * @param sdkKey           The SDK key, which can be found on dashboard.bugbattle.io
     * @param activationMethod Activation method, which triggers a new bug report.
     */
    @Deprecated
    public static BugBattle initialise(String sdkKey, BugBattleActivationMethod activationMethod, Application application) {
        return initWithToken(sdkKey, activationMethod, application);
    }

    /**
     * Manually start the bug reporting workflow. This is used, when you use the activation method "NONE".
     *
     * @throws BugBattleNotInitialisedException thrown when BugBattle is not initialised
     */
    @Override
    public void startBugReporting() throws BugBattleNotInitialisedException {
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
     * @param screenshot the image will be used instead of the current
     */
    @Override
    public void startBugReporting(Bitmap screenshot) throws BugBattleNotInitialisedException {
        if (instance != null) {
            BugBattleBug.getInstance().setScreenshot(screenshot);
            screenshotTaker.openScreenshot(screenshot);
        } else {
            throw new BugBattleNotInitialisedException("BugBattle is not initialised");
        }
    }

    /**
     * Send a silent bugreport in the background. Useful for automated ui tests.
     *
     * @param email       who sent the bug report
     * @param description description of the bug
     * @param severity    Severity of the bug "LOW", "MIDDLE", "HIGH"
     */
    @Override
    public void sendSilentBugReport(String email, String description, SEVERITY severity) {
        SilentBugReportUtil.createSilentBugReport(application, email, description, severity.name());
    }

    /**
     * Sets the API url to your internal Bugbattle server. Please make sure that the server is reachable within the network
     * If you use a http url pls add android:usesCleartextTraffic="true" to your main activity to allow cleartext traffic
     *
     * @param apiUrl url of the internal Bugbattle server
     */
    @Override
    public void setApiUrl(String apiUrl) {
        BugBattleConfig.getInstance().setApiUrl(apiUrl);
    }

    /**
     * Set/Prefill the email address for the user.
     *
     * @param email address, which is fileld in.
     */
    @Override
    public void setCustomerEmail(String email) {
        BugBattleBug.getInstance().setEmail(email);
    }

    /**
     * Set the main color of the bugbattle flow.
     * @param color this color is used to adapt ui. Use Hex format
     */
    @Override
    public void setColor(String color) {
        BugBattleConfig.getInstance().setColor(color);
    }

    /**
     * Set the language for the BugBattle Report Flow. Otherwise the default language is used.
     * Supported Languages "en", "es", "fr", "it", "de", "nl", "cz"
     *
     * @param language ISO Country Code eg. "cz," "en", "de", "es", "nl"
     */
    @Override
    public void setLanguage(String language) {
        BugBattleConfig.getInstance().setLanguage(language);
    }

    /**
     * Enable Replay function for BB
     * Use with care, check performance on phone
     */
    @Override
    public void enableReplays(boolean enable) {
        BugBattleConfig.getInstance().setEnableReplays(enable);
    }

    /**
     * Attaches custom data, which can be viewed in the BugBattle dashboard. New data will be merged with existing custom data.
     *
     * @param customData The data to attach to a bug report.
     * @author BugBattle
     */
    @Override
    public void appendCustomData(JSONObject customData) {
        BugBattleBug.getInstance().attachData(customData);
    }

    /**
     * Attach one key value pair to existing custom data.
     *
     * @param value The value you want to add
     * @param key   The key of the attribute
     * @author BugBattle
     */
    @Override
    public void setCustomData(String key, String value) {
        BugBattleBug.getInstance().setCustomData(key, value);
    }

    /**
     * Attach Data to the request. The Data will be merged into the body sent with the bugreport.
     * !!Existing keys can be overriten
     *
     * @param data Data, which is added
     */
    @Override
    public void attachData(JSONObject data) {
        BugBattleBug.getInstance().setCustomData(data);
    }

    /**
     * Removes one key from existing custom data.
     *
     * @param key The key of the attribute
     * @author BugBattle
     */
    @Override
    public void removeCustomDataForKey(String key) {
        BugBattleBug.getInstance().removeUserAttribute(key);
    }

    /**
     * Clears all custom data.
     */
    @Override
    public void clearCustomData() {
        BugBattleBug.getInstance().clearCustomData();
    }

    /**
     * This is called, when the bugbattle flow is started
     *
     * @param bugWillBeSentCallback is called when BB is opened
     */
    @Override
    public void setBugWillBeSentCallback(BugWillBeSentCallback bugWillBeSentCallback) {
        BugBattleConfig.getInstance().setBugWillBeSentCallback(bugWillBeSentCallback);
    }

    /**
     * This method is triggered, when the bugbattle flow is closed
     *
     * @param bugSentCallback this callback is called when the flow is called
     */
    @Override
    public void setBugSentCallback(BugSentCallback bugSentCallback) {
        BugBattleConfig.getInstance().setBugSentCallback(bugSentCallback);
    }

    /**
     * Customize the way, the Bitmap is generated. If this is overritten,
     * only the custom way is used
     *
     * @param getBitmapCallback get the Bitmap
     */
    @Override
    public void setBitmapCallback(GetBitmapCallback getBitmapCallback) {
        BugBattleConfig.getInstance().setGetBitmapCallback(getBitmapCallback);
    }

    /**
     * Log network traffic by logging it manually.
     *
     * @param urlConnection URL where the request is sent to
     * @param requestType   GET, POST, PUT, DELETE
     * @param status        status of the response (e.g. 200, 404)
     * @param duration      duration of the request
     * @param request       Add the data you want. e.g the body sent in the request
     * @param response      Response of the call. You can add just the information you want and need.
     */
    public void logNetwork(String urlConnection, RequestType requestType, int status, int duration, JSONObject request, JSONObject response) {
        BugBattleHttpInterceptor.log(urlConnection, requestType, status, duration, request, response);
    }

    /**
     * Register custom functions. This custom function can be configured in the widget, Form, Details of one step tab on app.bugbattle.io
     *
     * @param customAction what is executed when the custom step is pressed
     */
    @Override
    public void registerCustomAction(CustomActionCallback customAction) {
        BugBattleConfig.getInstance().registerCustomAction(customAction);
    }

    /**
     * Set Application Type
     *
     * @param applicationType "Android", "RN", "Flutter"
     */
    @Override
    public void setApplicationType(APPLICATIONTYPE applicationType) {
        BugBattleBug.getInstance().setApplicationtype(applicationType);
    }

    /**
     * Enables the privacy policy check.
     *
     * @param enable Enable the privacy policy.
     */
    public void enablePrivacyPolicy(boolean enable) {
        BugBattleConfig.getInstance().setPrivacyPolicyEnabled(enable);
    }

    /**
     * Sets a custom privacy policy url.
     *
     * @param privacyUrl The URL pointing to your privacy policy.
     */
    public void setPrivacyPolicyUrl(String privacyUrl) {
        BugBattleConfig.getInstance().setPrivacyPolicyUrl(privacyUrl);
    }

    /**
     * Severity of the bug. Can be used in the silent bug report.
     */
    public enum SEVERITY {
        LOW, MIDDLE, HIGH
    }

    public static class BugBattleListener implements OnHttpResponseListener {

        public BugBattleListener() {
            new ConfigLoader(this).execute(BugBattleBug.getInstance());
        }

        @Override
        public void onTaskComplete(int httpResponse) {
            if (useAutoconfig) {
                BugBattleConfig config = BugBattleConfig.getInstance();

                List<BugBattleActivationMethod> activationMethods = new LinkedList<>();
                if (config.isActivationMethodShake()) {
                    activationMethods.add(BugBattleActivationMethod.SHAKE);
                }

                if (config.isActivationMethodScreenshotGesture()) {
                    activationMethods.add(BugBattleActivationMethod.SCREENSHOT);
                }
                if (instance == null) {
                    instance = new BugBattle();
                }
                initBugBattle(BugBattleConfig.getInstance().getSdkKey(), activationMethods.toArray(new BugBattleActivationMethod[0]), application);
            }
        }
    }

    /**
     * Enables or disables the powered by Bugbattle logo.
     *
     * @param enable Enablesor disable the powered by Bugbattle logo.
     * @author BugBattle
     */
    @Override
    public void enablePoweredByBugbattle(boolean enable) {
        BugBattleConfig.getInstance().setShowPoweredBy(enable);
    }

    /**
     * Sets the main logo url.
     *
     * @param logoUrl The main logo url.
     * @author BugBattle
     */
    @Override
    public void setLogoUrl(String logoUrl) {
        BugBattleConfig.getInstance().setLogoUrl(logoUrl);
    }


    /**
     * Logs a custom event
     *
     * @param name Name of the event
     * @author BugBattle
     */
    @Override
    public void logEvent(String name) {
        BugBattleBug.getInstance().logEvent(name);
    }

    /**
     * Logs a custom event with data
     *
     * @param name Name of the event
     * @param data Data passed with the event.
     * @author BugBattle
     */
    @Override
    public void logEvent(String name, JSONObject data) {
        BugBattleBug.getInstance().logEvent(name, data);
    }

    /**
     * In order to pre-fill the customer's name,
     * we recommend using the following method.
     * This welcomes the user with his name and simplifies the feedback reporting,
     * *
     * @param name name of the customer
     */
    @Override
    public void setCustomerName(String name) {
        BugBattleBug.getInstance().setCustomerName(name);
    }
}
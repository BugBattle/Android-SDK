package gleap.io.gleap;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Gleap implements iGleap {
    private static Gleap instance;
    private static ScreenshotTaker screenshotTaker;
    private static Activity activity;
    private static Application application;

    private Gleap() {
    }

    /**
     * Init Gleap with the given properties
     */
    private static void initGleap(String sdkKey, GleapActivationMethod[] activationMethods, Application application) {
        try {
            //prepare Gleap
            Gleap.application = application;
            screenshotTaker = new ScreenshotTaker();
            ConsoleUtil.clearConsole();
            //init config and load from the server
            GleapConfig.getInstance().setSdkKey(sdkKey);

            //init Gleap bug
            GleapBug.getInstance().setPhoneMeta(new PhoneMeta(application.getApplicationContext()));

            Gleap.getInstance().enableReplays(GleapConfig.getInstance().isEnableReplays());

            //start activation methods
            List<GleapDetector> detectorList = GleapDetectorUtil.initDetectors(application, activity, activationMethods);

            if (GleapConfig.getInstance().isEnableReplays()) {
                ReplaysDetector replaysDetector = new ReplaysDetector(application);
                replaysDetector.initialize();
                detectorList.add(replaysDetector);
            }
            GleapConfig.getInstance().setGestureDetectors(detectorList);
            GleapDetectorUtil.resumeAllDetectors();
        } catch (Exception err) {
        }
    }



    /**
     * Get an instance of Gleap
     *
     * @return instance of Gleap
     */
    public static Gleap getInstance() {
        if (instance == null) {
            instance = new Gleap();
        }
        return instance;
    }

    /**
     * Auto-configures the Gleap SDK from the remote config.
     *
     * @param sdkKey      The SDK key, which can be found on dashboard.Gleap.io
     * @param application used to have context and access to take screenshot
     */
    public static void initialize(String sdkKey, Application application) {
        Gleap.application = application;
        GleapConfig.getInstance().setSdkKey(sdkKey);
        UserSessionController.initialize(application);
        new GleapListener();
    }

    public static void initialize(String sdkKey, GleapUserSession userSession,Application application) {
        Gleap.application = application;
        GleapConfig.getInstance().setSdkKey(sdkKey);
        UserSessionController userSessionController = UserSessionController.initialize(application);
        userSessionController.setGleapUserSession(userSession);
        new GleapListener();
    }

    /**
     * Manually start the bug reporting workflow. This is used, when you use the activation method "NONE".
     *
     * @throws GleapNotInitialisedException thrown when Gleap is not initialised
     */
    @Override
    public void startBugReporting() throws GleapNotInitialisedException {
        if (instance != null) {
            try {
                screenshotTaker.takeScreenshot();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new GleapNotInitialisedException("Gleap is not initialised");
        }
    }

    /**
     * Starts the bug reporting with a custom screenshot attached.
     *
     * @param screenshot the image will be used instead of the current
     */
    @Override
    public void startBugReporting(Bitmap screenshot) throws GleapNotInitialisedException {
        if (instance != null) {
            GleapBug.getInstance().setScreenshot(screenshot);
            screenshotTaker.openScreenshot(screenshot);
        } else {
            throw new GleapNotInitialisedException("Gleap is not initialised");
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
     * Sets the API url to your internal Gleap server. Please make sure that the server is reachable within the network
     * If you use a http url pls add android:usesCleartextTraffic="true" to your main activity to allow cleartext traffic
     *
     * @param apiUrl url of the internal Gleap server
     */
    @Override
    public void setApiUrl(String apiUrl) {
        GleapConfig.getInstance().setApiUrl(apiUrl);
    }

    /**
     * Set/Prefill the email address for the user.
     *
     * @param email address, which is fileld in.
     */
    @Override
    public void setCustomerEmail(String email) {
        GleapBug.getInstance().setEmail(email);
    }

    /**
     * Set the main color of the Gleap flow.
     *
     * @param color this color is used to adapt ui. Use Hex format
     */
    @Override
    public void setColor(String color) {
        GleapConfig.getInstance().setColor(color);
    }

    /**
     * Set the language for the Gleap Report Flow. Otherwise the default language is used.
     * Supported Languages "en", "es", "fr", "it", "de", "nl", "cz"
     *
     * @param language ISO Country Code eg. "cz," "en", "de", "es", "nl"
     */
    @Override
    public void setLanguage(String language) {
        GleapConfig.getInstance().setLanguage(language);
    }

    /**
     * Enable Replay function for BB
     * Use with care, check performance on phone
     */
    @Override
    public void enableReplays(boolean enable) {
        GleapConfig.getInstance().setEnableReplays(enable);
    }

    /**
     * Attaches custom data, which can be viewed in the Gleap dashboard. New data will be merged with existing custom data.
     *
     * @param customData The data to attach to a bug report.
     * @author Gleap
     */
    @Override
    public void appendCustomData(JSONObject customData) {
        GleapBug.getInstance().attachData(customData);
    }

    /**
     * Attach one key value pair to existing custom data.
     *
     * @param value The value you want to add
     * @param key   The key of the attribute
     * @author Gleap
     */
    @Override
    public void setCustomData(String key, String value) {
        GleapBug.getInstance().setCustomData(key, value);
    }

    /**
     * Attach Data to the request. The Data will be merged into the body sent with the bugreport.
     * !!Existing keys can be overriten
     *
     * @param data Data, which is added
     */
    @Override
    public void attachData(JSONObject data) {
        GleapBug.getInstance().setCustomData(data);
    }

    /**
     * Removes one key from existing custom data.
     *
     * @param key The key of the attribute
     * @author Gleap
     */
    @Override
    public void removeCustomDataForKey(String key) {
        GleapBug.getInstance().removeUserAttribute(key);
    }

    /**
     * Clears all custom data.
     */
    @Override
    public void clearCustomData() {
        GleapBug.getInstance().clearCustomData();
    }

    /**
     * This is called, when the Gleap flow is started
     *
     * @param bugWillBeSentCallback is called when BB is opened
     */
    @Override
    public void setBugWillBeSentCallback(BugWillBeSentCallback bugWillBeSentCallback) {
        GleapConfig.getInstance().setBugWillBeSentCallback(bugWillBeSentCallback);
    }

    /**
     * This method is triggered, when the Gleap flow is closed
     *
     * @param gleapSentCallback this callback is called when the flow is called
     */
    @Override
    public void setBugSentCallback(GleapSentCallback gleapSentCallback) {
        GleapConfig.getInstance().setBugSentCallback(gleapSentCallback);
    }

    /**
     * Customize the way, the Bitmap is generated. If this is overritten,
     * only the custom way is used
     *
     * @param getBitmapCallback get the Bitmap
     */
    @Override
    public void setBitmapCallback(GetBitmapCallback getBitmapCallback) {
        GleapConfig.getInstance().setGetBitmapCallback(getBitmapCallback);
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
        GleapHttpInterceptor.log(urlConnection, requestType, status, duration, request, response);
    }

    /**
     * Register custom functions. This custom function can be configured in the widget, Form, Details of one step tab on app.Gleap.io
     *
     * @param customAction what is executed when the custom step is pressed
     */
    @Override
    public void registerCustomAction(CustomActionCallback customAction) {
        GleapConfig.getInstance().registerCustomAction(customAction);
    }

    /**
     * Set Application Type
     *
     * @param applicationType "Android", "RN", "Flutter"
     */
    @Override
    public void setApplicationType(APPLICATIONTYPE applicationType) {
        GleapBug.getInstance().setApplicationtype(applicationType);
    }

    /**
     * Enables the privacy policy check.
     *
     * @param enable Enable the privacy policy.
     */
    public void enablePrivacyPolicy(boolean enable) {
        GleapConfig.getInstance().setPrivacyPolicyEnabled(enable);
    }

    /**
     * Sets a custom privacy policy url.
     *
     * @param privacyUrl The URL pointing to your privacy policy.
     */
    public void setPrivacyPolicyUrl(String privacyUrl) {
        GleapConfig.getInstance().setPrivacyPolicyUrl(privacyUrl);
    }

    /**
     * Severity of the bug. Can be used in the silent bug report.
     */
    public enum SEVERITY {
        LOW, MIDDLE, HIGH
    }

    public static class GleapListener implements OnHttpResponseListener {

        public GleapListener() {
            new ConfigLoader(this).execute(GleapBug.getInstance());
            new GleapUserSessionLoader().execute();
        }

        @Override
        public void onTaskComplete(int httpResponse) {

            GleapConfig config = GleapConfig.getInstance();

            List<GleapActivationMethod> activationMethods = new LinkedList<>();
            if (config.isActivationMethodShake()) {
                activationMethods.add(GleapActivationMethod.SHAKE);
            }

            if (config.isActivationMethodScreenshotGesture()) {
                activationMethods.add(GleapActivationMethod.SCREENSHOT);
            }
            if (instance == null) {
                instance = new Gleap();
            }
            initGleap(GleapConfig.getInstance().getSdkKey(), activationMethods.toArray(new GleapActivationMethod[0]), application);

        }
    }

    /**
     * Enables or disables the powered by Gleap logo.
     *
     * @param enable Enablesor disable the powered by Gleap logo.
     * @author Gleap
     */
    @Override
    public void enablePoweredByGleap(boolean enable) {
        GleapConfig.getInstance().setShowPoweredBy(enable);
    }

    /**
     * Sets the main logo url.
     *
     * @param logoUrl The main logo url.
     * @author Gleap
     */
    @Override
    public void setLogoUrl(String logoUrl) {
        GleapConfig.getInstance().setLogoUrl(logoUrl);
    }


    /**
     * Logs a custom event
     *
     * @param name Name of the event
     * @author Gleap
     */
    @Override
    public void logEvent(String name) {
        GleapBug.getInstance().logEvent(name);
    }

    /**
     * Logs a custom event with data
     *
     * @param name Name of the event
     * @param data Data passed with the event.
     * @author Gleap
     */
    @Override
    public void logEvent(String name, JSONObject data) {
        GleapBug.getInstance().logEvent(name, data);
    }

    /**
     * In order to pre-fill the customer's name,
     * we recommend using the following method.
     * This welcomes the user with his name and simplifies the feedback reporting,
     * *
     *
     * @param name name of the customer
     */
    @Override
    public void setCustomerName(String name) {
        GleapBug.getInstance().setCustomerName(name);
    }
}
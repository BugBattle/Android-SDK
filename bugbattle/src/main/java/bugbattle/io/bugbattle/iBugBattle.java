package bugbattle.io.bugbattle;

import android.graphics.Bitmap;

import org.json.JSONObject;

import bugbattle.io.bugbattle.callbacks.BugSentCallback;
import bugbattle.io.bugbattle.callbacks.BugWillBeSentCallback;
import bugbattle.io.bugbattle.callbacks.GetBitmapCallback;
import bugbattle.io.bugbattle.exceptions.BugBattleNotInitialisedException;
import bugbattle.io.bugbattle.model.APPLICATIONTYPE;
import bugbattle.io.bugbattle.model.CustomAction;
import bugbattle.io.bugbattle.model.RequestType;

interface iBugBattle {

    /**
     * Invoke Bug Reporting
     */

    /**
     * Manually start the bug reporting workflow. This is used, when you use the activation method "NONE".
     *
     * @throws BugBattleNotInitialisedException thrown when BugBattle is not initialised
     */
    void startBugReporting() throws BugBattleNotInitialisedException;

    /**
     * Starts the bug reporting with a custom screenshot attached.
     *
     * @param screenshot the image will be used instead of the current
     */
    void startBugReporting(Bitmap screenshot) throws BugBattleNotInitialisedException;

    /**
     * Send a silent bugreport in the background. Useful for automated ui tests.
     *
     * @param email       who sent the bug report
     * @param description description of the bug
     * @param severity    Severity of the bug "LOW", "MIDDLE", "HIGH"
     */
    void sendSilentBugReport(String email, String description, BugBattle.SEVERITY severity);

    /**
     * Configure Bugbattle
     */
    /**
     * Sets the API url to your internal Bugbattle server. Please make sure that the server is reachable within the network
     * If you use a http url pls add android:usesCleartextTraffic="true" to your main activity to allow cleartext traffic
     *
     * @param apiUrl url of the internal Bugbattle server
     */
    void setApiUrl(String apiUrl);

    /**
     * Set/Prefill the email address for the user.
     *
     * @param email address, which is fileld in.
     */
    void setCustomerEmail(String email);

    /**
     * Change the color of the appearance of the UI.
     *
     * @param color
     */
    void setNavigationTint(String color);

    /**
     * Set the language for the BugBattle Report Flow. Otherwise the default language is used.
     * Supported Languages "en", "es", "fr", "it", "de", "nl", "cz"
     *
     * @param language ISO Country Code eg. "cz," "en", "de", "es", "nl"
     */
    void setLanguage(String language);

    /**
     * Enable Replay function for BB
     * Use with care, check performance on phone
     */
    void enableReplays(boolean enable) throws BugBattleNotInitialisedException;

    /**
     * Set Application Type
     *
     * @param applicationType "Android", "RN", "Flutter"
     */
    void setApplicationType(APPLICATIONTYPE applicationType);

    /**
     * Custom Data
     */
    @Deprecated
    void attachCustomData(JSONObject customData);

    void setUserAttribute(String key, String value);

    void attachData(JSONObject data);

    void removeCustomDataForKey(String key);

    void clearCustomData();

    /**
     * Callbacks
     */

    /**
     * This is called, when the bugbattle flow is started
     *
     * @param bugWillBeSentCallback is called when BB is opened
     */
    void setBugWillBeSentCallback(BugWillBeSentCallback bugWillBeSentCallback);

    /**
     * This method is triggered, when the bugbattle flow is closed
     *
     * @param bugSentCallback this callback is called when the flow is called
     */
    void setBugSentCallback(BugSentCallback bugSentCallback);

    /**
     * Customize the way, the Bitmap is generated. If this is overritten,
     * only the custom way is used
     *
     * @param getBitmapCallback get the Bitmap
     */
    void setBitmapCallback(GetBitmapCallback getBitmapCallback);

    /**
     * Network
     */
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
    void logNetwork(String urlConnection, RequestType requestType, int status, int duration, JSONObject request, JSONObject response);

    //not implemented yet
    //void shakeInvocation();
    //void attachScreenshot(Bitmap screenshot);
    //Bitmap getAttachedScreenshot();

    void registerCustomAction(CustomAction customAction);
}

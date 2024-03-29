package gleap.io.gleap;

import android.graphics.Bitmap;

import org.json.JSONObject;

interface iGleap {

    /**
     * Invoke Bug Reporting
     */

    /**
     * Manually start the bug reporting workflow. This is used, when you use the activation method "NONE".
     *
     * @throws GleapNotInitialisedException thrown when BugBattle is not initialised
     */
    void startBugReporting() throws GleapNotInitialisedException;

    /**
     * Starts the bug reporting with a custom screenshot attached.
     *
     * @param screenshot the image will be used instead of the current
     */
    void startBugReporting(Bitmap screenshot) throws GleapNotInitialisedException;

    /**
     * Send a silent bugreport in the background. Useful for automated ui tests.
     *
     * @param email       who sent the bug report
     * @param description description of the bug
     * @param severity    Severity of the bug "LOW", "MIDDLE", "HIGH"
     */
    void sendSilentBugReport(String email, String description, Gleap.SEVERITY severity);

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
     * Set the main color of the bugbattle flow.
     * @param color this color is used to adapt ui. Use Hex format
     */
    void setColor(String color);

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
    void enableReplays(boolean enable) throws GleapNotInitialisedException;

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
    void appendCustomData(JSONObject customData);

    void setCustomData(String key, String value);

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
     * @param gleapSentCallback this callback is called when the flow is called
     */
    void setBugSentCallback(GleapSentCallback gleapSentCallback);

    /**
     * Customize the way, the Bitmap is generated. If this is overritten,
     * only the custom way is used
     *
     * @param getBitmapCallback get the Bitmap
     */
    void setBitmapCallback(GetBitmapCallback getBitmapCallback);

    /**
     * Enables the privacy policy check.
     *
     * @param enable Enable the privacy policy.
     */
    void enablePrivacyPolicy(boolean enable);

    /**
     * Sets a custom privacy policy url.
     *
     * @param privacyUrl The URL pointing to your privacy policy.
     */
    void setPrivacyPolicyUrl(String privacyUrl);

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

    /**
     * Register a custom function, which can be called from the bug report flow
     *
     * @param customAction implement the callback
     */
    void registerCustomAction(CustomActionCallback customAction);

    /**
     * Enables or disables the powered by Bugbattle logo.
     *
     * @param enable Enablesor disable the powered by Bugbattle logo.
     * @author BugBattle
     */
    void enablePoweredByGleap(boolean enable);

    /**
     * Sets the main logo url.
     *
     * @param logoUrl The main logo url.
     * @author BugBattle
     */
    void setLogoUrl(String logoUrl);

    /**
     * Logs a custom event
     *
     * @param name Name of the event
     * @author BugBattle
     */
    void logEvent(String name);

    /**
     * Logs a custom event with data
     *
     * @param name Name of the event
     * @param data Data passed with the event.
     * @author BugBattle
     */
    void logEvent(String name, JSONObject data);

    /**
     * In order to pre-fill the customer's name,
     * we recommend using the following method.
     * This welcomes the user with his name and simplifies the feedback reporting,
     * *
     * @param name name of the customer
     */
    void setCustomerName(String name);
}

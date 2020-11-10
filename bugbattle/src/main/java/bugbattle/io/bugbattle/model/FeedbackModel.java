package bugbattle.io.bugbattle.model;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import bugbattle.io.bugbattle.controller.StepsToReproduce;
import bugbattle.io.bugbattle.service.BBDetector;
import bugbattle.io.bugbattle.service.LogReader;

/**
 * Contains all relevant information gathered in the background.
 */
public class FeedbackModel {
    private boolean isDisabled = false;
    private static FeedbackModel instance;

    private String sdkKey;
    private String userEmail;
    private String description;
    private String severity;

    private String apiUrl = "https://apidev.bugbattle.io";
    private Bitmap screenshot;

    private JSONObject customData;
    private @Nullable
    PhoneMeta phoneMeta;
    private LogReader logReader;
    private StepsToReproduce stepsToReproduce;
    private BBDetector gestureDetector;

    private boolean privacyEnabled = false;
    private String privacyUrl = "https://www.bugbattle.io/privacy-policy";


    private FeedbackModel() {
        logReader = new LogReader();
        stepsToReproduce = StepsToReproduce.getInstance();
        customData = new JSONObject();
    }

    public static FeedbackModel getInstance() {
        if (instance == null) {
            instance = new FeedbackModel();
        }
        return instance;
    }

    public @Nullable
    PhoneMeta getPhoneMeta() {
        return phoneMeta;
    }

    public String getSdkKey() {
        return sdkKey;
    }

    public void setSdkKey(String key) {
        sdkKey = key;
    }

    public void setScreenshot(Bitmap screenshot) {
        this.screenshot = screenshot;
    }

    public Bitmap getScreenshot() {
        return screenshot;
    }

    public JSONArray getLogs() {
        return logReader.readLog();
    }

    public JSONArray getStepsToReproduce() {
        return stepsToReproduce.getSteps();
    }

    public String getEmail() {
        return userEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JSONObject getCustomData() {
        return customData;
    }

    public void setCustomData(JSONObject customData) {
        this.customData = customData;
    }

    public BBDetector getGestureDetector() {
        return gestureDetector;
    }

    public void setGestureDetector(BBDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }


    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public void enablePrivacyPolicy(boolean enable) {
        privacyEnabled = enable;
    }

    public boolean isPrivacyEnabled() {
        return privacyEnabled;
    }

    public void setPrivacyPolicyUrl(String privacyUrl) {
        this.privacyUrl = privacyUrl;
    }

    public String getPrivacyUrl() {
        return privacyUrl;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }
}

package bugbattle.io.bugbattle.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import bugbattle.io.bugbattle.controller.StepsToReproduce;
import bugbattle.io.bugbattle.service.LogReader;
import bugbattle.io.bugbattle.service.ShakeGestureDetector;

/**
 * Contains all relevant information gathered in the background.
 */
public class FeedbackModel {
    private static FeedbackModel instance;

    private Context context;

    private String sdkKey;
    private String userEmail;
    private String description;
    private String severity;

    //default color of the statusbar (bugbattle blue)
    private String appBarColor = "#019AE8";
    private String apiUrl = "https://apidev.bugbattle.io";
    private Bitmap screenshot;

    private JSONObject customData;
    private @Nullable
    PhoneMeta phoneMeta;
    private LogReader logReader;
    private StepsToReproduce stepsToReproduce;
    private ShakeGestureDetector shakeGestureDetector;


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
        return logReader.readLog(context);
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

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        phoneMeta = new PhoneMeta(context);
    }

    public String getAppBarColor() {
        return appBarColor;
    }

    public void setAppBarColor(String appBarColor) {
        this.appBarColor = appBarColor;
    }

    public JSONObject getCustomData() {
        return customData;
    }

    public void setCustomData(JSONObject customData) {
        this.customData = customData;
    }

    public ShakeGestureDetector getShakeGestureDetector() {
        return shakeGestureDetector;
    }

    public void setShakeGestureDetector(ShakeGestureDetector shakeGestureDetector) {
        this.shakeGestureDetector = shakeGestureDetector;
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
}

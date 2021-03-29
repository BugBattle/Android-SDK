package bugbattle.io.bugbattle.model;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import bugbattle.io.bugbattle.BugSentCallback;
import bugbattle.io.bugbattle.BugWillBeSentCallback;
import bugbattle.io.bugbattle.GetBitmapCallback;
import bugbattle.io.bugbattle.controller.StepsToReproduce;
import bugbattle.io.bugbattle.service.BBDetector;
import bugbattle.io.bugbattle.service.LogReader;

/**
 * Contains all relevant information gathered in the background.
 */
public class FeedbackModel {
    private Date startUpDate = new Date();
    private boolean isDisabled = false;
    private static FeedbackModel instance;
    private String language = "";

    private String sdkKey;
    private String userEmail;
    private String description;
    private String severity;
    private APPLICATIONTYPE applicationtype = APPLICATIONTYPE.NATIVE;

    private String apiUrl = "https://api.bugbattle.io";
    private Bitmap screenshot;
    private Replay replay;
    private JSONObject customData;
    private JSONObject data;
    private @Nullable
    PhoneMeta phoneMeta;
    private LogReader logReader;
    private StepsToReproduce stepsToReproduce;
    private List<BBDetector> gestureDetectors;

    private boolean privacyEnabled = false;
    private String privacyUrl = "https://www.bugbattle.io/privacy-policy";

    private BugSentCallback bugSentCallback;
    private BugWillBeSentCallback bugWillBeSentCallback;
    private GetBitmapCallback getBitmapCallback;

    private List<Networklog> networklogs = new LinkedList<>();

    private FeedbackModel() {
        logReader = new LogReader();
        stepsToReproduce = StepsToReproduce.getInstance();
        customData = new JSONObject();
        replay = new Replay(30, 1000);
    }

    public static FeedbackModel getInstance() {
        if (instance == null) {
            instance = new FeedbackModel();
        }
        return instance;
    }

    public void setPhoneMeta(PhoneMeta phoneMeta) {
        this.phoneMeta = phoneMeta;
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

    public JSONArray getLogs() throws ParseException {
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

    public List<BBDetector> getGestureDetectors() {
        return gestureDetectors;
    }

    public void setGestureDetectors(List<BBDetector> gestureDetectors) {
        this.gestureDetectors = gestureDetectors;
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

    public BugSentCallback getBugSentCallback() {
        return bugSentCallback;
    }

    public void setBugSentCallback(BugSentCallback bugSentCallback) {
        this.bugSentCallback = bugSentCallback;
    }

    public BugWillBeSentCallback getBugWillBeSentCallback() {
        return bugWillBeSentCallback;
    }

    public void setBugWillBeSentCallback(BugWillBeSentCallback bugWillBeSentCallback) {
        this.bugWillBeSentCallback = bugWillBeSentCallback;
    }

    public GetBitmapCallback getGetBitmapCallback() {
        return getBitmapCallback;
    }

    public void setGetBitmapCallback(GetBitmapCallback getBitmapCallback) {
        this.getBitmapCallback = getBitmapCallback;
    }

    public APPLICATIONTYPE getApplicationtype() {
        return applicationtype;
    }

    public void setApplicationtype(APPLICATIONTYPE applicationtype) {
        this.applicationtype = applicationtype;
    }

    public Replay getReplay() {
        return replay;
    }

    public void setReplay(Replay replay) {
        this.replay = replay;
    }

    public Date getStartUpDate() {
        return startUpDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void addRequest(Networklog networklog) {
        networklogs.add(networklog);
    }

    public JSONArray getNetworklogs() {
        JSONArray requestArry = new JSONArray();
        try {
            for (Networklog networklog : networklogs) {
                requestArry.put(networklog.toJSON());
            }
        } catch (Exception err) {
            System.out.println(err);
        }
        return requestArry;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject getData() {
        return this.data;
    }
}

package bugbattle.io.bugbattle.model;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import bugbattle.io.bugbattle.controller.StepsToReproduce;
import bugbattle.io.bugbattle.service.LogReader;
import bugbattle.io.bugbattle.util.JsonUtil;

/**
 * Contains all relevant information gathered in the background.
 */
public class BugBattleBug {
    private static BugBattleBug instance;

    //bug specific data
    private APPLICATIONTYPE applicationtype = APPLICATIONTYPE.NATIVE;
    private Date startUpDate = new Date();
    private boolean isDisabled = false;
    private String language = "";
    private String severity;
    private String userEmail;
    private String description;
    private Bitmap screenshot;
    private Replay replay;
    private JSONObject customData;
    private JSONObject data;
    private @Nullable
    PhoneMeta phoneMeta;
    private LogReader logReader;
    private StepsToReproduce stepsToReproduce;

    private List<Networklog> networklogs = new LinkedList<>();

    private BugBattleBug() {
        logReader = new LogReader();
        stepsToReproduce = StepsToReproduce.getInstance();
        customData = new JSONObject();
        replay = new Replay(30, 1000);
    }

    public static BugBattleBug getInstance() {
        if (instance == null) {
            instance = new BugBattleBug();
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

    public void setEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public JSONObject getCustomData() {
        return customData;
    }

    public void setCustomData(JSONObject customData) {
        this.customData = customData;
    }


    public void setUserAttribute(String key, String value) {
        try {
            this.customData.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void attachData(JSONObject data) {
        this.customData = JsonUtil.mergeJSONObjects(this.customData, data);
    }

    public void removeUserAttribute(String key) {
        this.customData.remove(key);
    }

    public void clearCustomData() {
        this.customData = new JSONObject();
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
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
        networklogs = new LinkedList<>();
        return requestArry;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject getData() {
        return this.data;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

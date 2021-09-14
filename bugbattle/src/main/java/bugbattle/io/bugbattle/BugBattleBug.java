package bugbattle.io.bugbattle;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static bugbattle.io.bugbattle.DateUtil.dateToString;

/**
 * Contains all relevant information gathered in the background.
 */
class BugBattleBug {
    private static BugBattleBug instance;
    private Application application;
    //bug specific data
    private APPLICATIONTYPE applicationtype = APPLICATIONTYPE.NATIVE;
    private final Date startUpDate = new Date();
    private boolean isDisabled = false;
    private String language = "";
    private String severity = "MEDIUM";
    private String userEmail;
    private String userName;
    private String silentBugreportEmail;
    private String description;
    private Bitmap screenshot;
    private Replay replay;
    private JSONObject customData;
    private JSONObject data;
    private @Nullable
    PhoneMeta phoneMeta;
    private final LogReader logReader;

    private final JSONArray customEventLog = new JSONArray();

    private List<Networklog> networklogs = new LinkedList<>();

    private BugBattleBug() {
        logReader = new LogReader();
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

    public String getEmail() {
        return userEmail;
    }

    public void setEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setCustomerName(String name){
        this.userName = name;
    }

    public String getCustomerName() {
        return userName;
    }

    public JSONObject getCustomData() {
        return customData;
    }

    public void setCustomData(JSONObject customData) {
        this.customData = customData;
    }


    public void setCustomData(String key, String value) {
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
           err.printStackTrace();
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

    public String getSilentBugreportEmail() {
        return silentBugreportEmail;
    }

    public void setSilentBugreportEmail(String silentBugreportEmail) {
        this.silentBugreportEmail = silentBugreportEmail;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void logEvent(String name, JSONObject data) {
        JSONObject event = new JSONObject();
        try {
            event.put("date", dateToString(new Date()));
            event.put("name", name);
            event.put("data", data);
            customEventLog.put(event);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void logEvent(String name) {
        JSONObject event = new JSONObject();
        try {
            event.put("date", dateToString(new Date()));
            event.put("name", name);
            customEventLog.put(event);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JSONArray getCustomEventLog() {
        return customEventLog;
    }
}

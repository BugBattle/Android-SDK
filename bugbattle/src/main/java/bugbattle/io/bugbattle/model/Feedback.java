package bugbattle.io.bugbattle.model;

import android.content.Context;
import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONObject;

import bugbattle.io.bugbattle.controller.StepsToReproduce;
import bugbattle.io.bugbattle.service.LogReader;
import bugbattle.io.bugbattle.service.ShakeGestureDetector;

/**
 * Contains all relevant information gathered in the background.
 */
public class Feedback {
    private Context context;
    private String sdkKey;
    private Bitmap image;
    private String email;
    private String description;
    private static Feedback instance;
    private PhoneMeta phoneMeta;
    private LogReader logReader;
    private String appBarColor = "#0169ff";
    private JSONObject customData;
    private ShakeGestureDetector shakeGestureDetector;
    private String severity;

    private StepsToReproduce stepsToReproduce;

    private Feedback()  {
        phoneMeta = PhoneMeta.init();
        logReader = new LogReader();
        stepsToReproduce = StepsToReproduce.getInstance();
        customData = new JSONObject();
    }

    public static Feedback init() {
        if(instance == null) {
            instance = new Feedback();
        }
        return instance;
    }

    public static Feedback getInstance() {
        return instance;
    }
    public PhoneMeta getPhoneMeta() {
        return phoneMeta;
    }
    public String getSdkKey() {
        return sdkKey;
    }

    public  void setSdkKey(String key) {
        sdkKey = key;
    }
    public void setImage(Bitmap img) {
        image = img;
    }

    public Bitmap getImage() {
        return image;
    }
    public JSONArray getLogs() {
        return logReader.readLog();
    }
    public JSONArray getStepsToReproduce() {
        return stepsToReproduce.getSteps();
    }

    public String getEmail() {
        return email;
    }
    public String getDescription() {
        return description;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
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
}

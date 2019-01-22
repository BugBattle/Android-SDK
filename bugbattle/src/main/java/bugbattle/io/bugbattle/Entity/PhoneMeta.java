package bugbattle.io.bugbattle.Entity;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import bugbattle.io.bugbattle.BuildConfig;

public class PhoneMeta {
    private static double startTime;
    private static String deviceModel;
    private static String deviceName;
    private static String deviceIdentifier;
    private static String bundleID;
    private static String systemName;
    private static String systemVersion;
    private static String buildVersionNumber;
    private static String releaseVersionNumber;
    private static PhoneMeta phoneMeta;

    private PhoneMeta(){
        startTime = new Date().getTime();
        getPhoneMeta();

    }

    public static PhoneMeta init() {
        if(phoneMeta == null) {
            phoneMeta = new PhoneMeta();
        }
        return phoneMeta;
    }

    public JSONObject getJSONObj() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("deviceModel", deviceModel);
        obj.put("deviceName", deviceName);
        obj.put("deviceIdentifier", deviceModel);
        obj.put("bundleID", bundleID);
        obj.put("systemName", systemName);
        obj.put("systemVersion", systemVersion);
        obj.put("buildVersionNumber", buildVersionNumber);
        obj.put("releaseVersionNumber", releaseVersionNumber);
        obj.put("sessionDuration", calculateDuration());
        return obj;
    }

    private void getPhoneMeta() {
        deviceModel = Build.MODEL;
        deviceName = Build.MODEL;
        deviceIdentifier= "";
        bundleID = BuildConfig.APPLICATION_ID;
        systemName = "Android";
        systemVersion = Build.VERSION.RELEASE;
        buildVersionNumber = Integer.toString(BuildConfig.VERSION_CODE);
        releaseVersionNumber = BuildConfig.VERSION_NAME;
    }

    private static String calculateDuration() {
        double timeDif = (new Date().getTime()-startTime )/1000;
        return Double.toString(timeDif);
    }
}

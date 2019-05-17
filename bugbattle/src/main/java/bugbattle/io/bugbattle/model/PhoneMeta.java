package bugbattle.io.bugbattle.model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

import bugbattle.io.bugbattle.BuildConfig;

/**
 * Collected information, gathered from the phone
 */
public class PhoneMeta {

    private static double startTime;
    private static String deviceModel;
    private static String deviceName;
    private static String bundleID;
    private static String systemName;
    private static String systemVersion;
    private static String buildVersionNumber;
    private static String releaseVersionNumber;

    public PhoneMeta() {
        startTime = new Date().getTime();
        getPhoneMeta();
    }

    /**
     * get the meta information for the phone
     *
     * @return the metainformation gathered from the phone
     * @throws JSONException
     */
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
        obj.put("networkStatus", getNetworkStatus());
        return obj;
    }

    private void getPhoneMeta() {
        deviceModel = Build.MODEL;
        deviceName = Build.MODEL;
        bundleID = BuildConfig.APPLICATION_ID;
        systemName = "Android";
        systemVersion = Build.VERSION.RELEASE;
        buildVersionNumber = Integer.toString(BuildConfig.VERSION_CODE);
        releaseVersionNumber = BuildConfig.VERSION_NAME;
    }

    private static String calculateDuration() {
        double timeDif = (new Date().getTime() - startTime) / 1000;
        return Double.toString(timeDif);
    }

    /**
     * The phone network state is only gathered, if the ACCESS_NETWORK_STATE is requested in the AndroidManifest.xml
     *
     * @return status of the network
     */
    private static JSONObject getNetworkStatus() {
        JSONObject result = new JSONObject();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_NETWORK_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                ConnectivityManager cm =
                        (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                //Only called when the permission is granted
                @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                result.put("isConnected", isConnected);
                result.put("type", activeNetwork.getTypeName());
                result.put("subType", activeNetwork.getSubtypeName());
                return result;
            } catch (JSONException ex) {
                return result;
            }
        } else {
            return result;
        }
    }

    private static Activity getActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);

            Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
            if (activities == null)
                return null;

            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    return (Activity) activityField.get(activityRecord);
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

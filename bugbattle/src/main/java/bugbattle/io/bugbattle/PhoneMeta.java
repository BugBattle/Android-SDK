package bugbattle.io.bugbattle;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

class PhoneMeta {
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
    private static FeedbackService service;

    private PhoneMeta() {
        startTime = new Date().getTime();
        getPhoneMeta();

    }

    public static PhoneMeta init() {
        if (phoneMeta == null) {
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
        obj.put("networkStatus", getNetworkStatus());
        return obj;
    }

    private void getPhoneMeta() {
        deviceModel = Build.MODEL;
        deviceName = Build.MODEL;
        deviceIdentifier = "";
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

    private static JSONObject getNetworkStatus() {
        service = FeedbackService.getInstance();
        JSONObject result = new JSONObject();
        if (ContextCompat.checkSelfPermission(service.getMainActivity(), Manifest.permission.ACCESS_NETWORK_STATE)
                == PackageManager.PERMISSION_GRANTED) {

            try {
                ConnectivityManager cm =
                        (ConnectivityManager) service.getMainActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
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
}

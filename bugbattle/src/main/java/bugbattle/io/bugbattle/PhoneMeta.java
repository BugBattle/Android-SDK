package bugbattle.io.bugbattle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

import static bugbattle.io.bugbattle.ActivityUtil.getCurrentActivity;


/**
 * Collected information, gathered from the phone
 */
class PhoneMeta {
    private final Context context;
    private static double startTime;
    private static String deviceModel;
    private static String deviceName;
    private static String lastScreenName;
    private static String bundleID;
    private static String systemName;
    private static String systemVersion;
    private static String buildVersionNumber;
    private static String releaseVersionNumber;
    private static final String sdkVersion = BuildConfig.VERSION_NAME;

    public PhoneMeta(@NonNull Context context) {
        startTime = new Date().getTime();
        this.context = context;
        getPhoneMeta();
    }

    /**
     * get the meta information for the phone
     *
     * @return the metainformation gathered from the phone
     * @throws JSONException cant create JSON Object
     */
    public JSONObject getJSONObj() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("sessionDuration", calculateDuration());
        obj.put("releaseVersionNumber", releaseVersionNumber);
        obj.put("deviceModel", deviceModel);
        obj.put("deviceName", deviceName);
        obj.put("deviceIdentifier", deviceModel);
        obj.put("bundleID", bundleID);
        obj.put("systemName", systemName);
        obj.put("systemVersion", systemVersion);
        obj.put("buildVersionNumber", buildVersionNumber);
        obj.put("lastScreenName", lastScreenName);
        obj.put("networkStatus", getNetworkStatus());
        obj.put("preferredUserLocale", getLocale());
        obj.put("sdkVersion", sdkVersion);

        String applicationType = "Native";
        if (BugBattleBug.getInstance().getApplicationtype() == APPLICATIONTYPE.FLUTTER) {
            applicationType = "Flutter";
        }
        if (BugBattleBug.getInstance().getApplicationtype() == APPLICATIONTYPE.REACTNATIVE) {
            applicationType = "ReactNative";
        }
        obj.put("applicationType", applicationType);
        return obj;
    }

    private void getPhoneMeta() {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager != null) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                buildVersionNumber = Integer.toString(packageInfo.versionCode);
                releaseVersionNumber = packageInfo.versionName;
                bundleID = packageInfo.packageName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                //    buildVersionNumber = Integer.toString(BuildConfig.VERSION_CODE);
                //   releaseVersionNumber = BuildConfig.VERSION_NAME;
                bundleID = context.getPackageName();

            }
        }
        if (getCurrentActivity() != null) {
            lastScreenName = getCurrentActivity().getClass().getSimpleName();
        }
        deviceModel = Build.MODEL;
        deviceName = Build.DEVICE;
        systemName = "Android";
        systemVersion = Build.VERSION.RELEASE;
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
    private String getNetworkStatus() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //Only called when the permission is granted
            @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork.getTypeName();

        } else {
            return "";
        }
    }

    public String getLocale() {
        return Locale.getDefault().getLanguage();
    }

    public void setLastScreen(String lastScreenName) {
        PhoneMeta.lastScreenName = lastScreenName;
    }
}

package bugbattle.io.bugbattle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Configuration received by the server
 */
class BugBattleConfig {
    private static BugBattleConfig instance;

    //bb config
    private String apiUrl = "https://api.bugbattle.io";
    private String sdkKey = "";
    private BugSentCallback bugSentCallback;
    private BugWillBeSentCallback bugWillBeSentCallback;
    private GetBitmapCallback getBitmapCallback;
    private List<BBDetector> gestureDetectors;
    private String privacyPolicyUrl = "";
    private boolean isPrivacyPolicyEnabled = false;

    //user config
    private String color = "#0693E3";
    private boolean enableConsoleLogs = true;
    private boolean enableReplays = false;
    private boolean activationMethodShake = false;
    private boolean activationMethodScreenshotGesture = false;
    private String language = "en";
    private CustomActionCallback customAction;
    private String logoUrl;
    private boolean showPoweredBy = true;
    private boolean poweredByForced = false;

    private BugBattleConfig() {
    }

    public static BugBattleConfig getInstance() {
        if (instance == null) {
            instance = new BugBattleConfig();
        }
        return instance;
    }

    /**
     * Read Values from the config
     *
     * @param config response from the server with all the configuration data in it
     */
    public void initConfig(JSONObject config) {
        try {
            this.color = config.getString("color");
            this.enableConsoleLogs = config.getBoolean("enableConsoleLogs");
            this.enableReplays = config.getBoolean("enableReplays");
            this.activationMethodShake = config.getBoolean("activationMethodShake");
            this.activationMethodScreenshotGesture = config.getBoolean("activationMethodScreenshotGesture");
            if (!poweredByForced) {
                this.showPoweredBy = !config.getBoolean("hideBugBattleLogo");
            }
            this.logoUrl = config.getString("logo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getSdkKey() {
        return sdkKey;
    }

    public void setSdkKey(String sdkKey) {
        this.sdkKey = sdkKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public List<BBDetector> getGestureDetectors() {
        return gestureDetectors;
    }

    public void setGestureDetectors(List<BBDetector> gestureDetectors) {
        this.gestureDetectors = gestureDetectors;
    }

    public boolean isActivationMethodShake() {
        return activationMethodShake;
    }

    public boolean isActivationMethodScreenshotGesture() {
        return activationMethodScreenshotGesture;
    }

    public boolean isEnableConsoleLogs() {
        return enableConsoleLogs;
    }

    public boolean isEnableReplays() {
        return enableReplays;
    }

    public void setEnableReplays(boolean enableReplays) {
        this.enableReplays = enableReplays;
    }

    public void registerCustomAction(CustomActionCallback customAction) {
        this.customAction = customAction;
    }

    public CustomActionCallback getCustomActions() {
        return customAction;
    }

    public String getPrivacyPolicyUrl() {
        return privacyPolicyUrl;
    }

    public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
        this.privacyPolicyUrl = privacyPolicyUrl;
    }

    public boolean isPrivacyPolicyEnabled() {
        return isPrivacyPolicyEnabled;
    }

    public void setPrivacyPolicyEnabled(boolean privacyPolicyEnabled) {
        isPrivacyPolicyEnabled = privacyPolicyEnabled;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public boolean isShowPoweredBy() {
        return showPoweredBy;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void setShowPoweredBy(boolean showPoweredBy) {
        this.poweredByForced = true;
        this.showPoweredBy = showPoweredBy;
    }
}

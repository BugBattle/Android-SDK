package bugbattle.io.bugbattle;

import android.app.Activity;
import android.graphics.Bitmap;

import org.json.JSONArray;

 class FeedbackService {
    private Activity mainActivity;
    private String sdkKey;
    private Bitmap image;
    private String email;
    private String description;
    private static FeedbackService instance;
    private PhoneMeta phoneMeta;
    private LogReader logReader;
    private StepsToReproduce stepsToReproduce;

    private FeedbackService()  {
        phoneMeta = PhoneMeta.init();
        logReader = new LogReader();
        stepsToReproduce = StepsToReproduce.getInstance();
    }

    public static FeedbackService init() {
        if(instance == null) {
            instance = new FeedbackService();
        }
        return instance;
    }

    public static FeedbackService getInstance() {
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

    public Activity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }
}

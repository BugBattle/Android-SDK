package bugbattle.io.bugbattle_android_sdk;

import android.app.Application;

import org.json.JSONException;
import org.json.JSONObject;

import bugbattle.io.bugbattle.BugBattle;
import bugbattle.io.bugbattle.controller.BugBattleActivationMethod;
import bugbattle.io.bugbattle.model.CustomAction;
import bugbattle.io.bugbattle.model.RequestType;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BugBattle.getInstance().enableReplays(true);
        // BugBattle.autoConfigure("9KaIxwf6XU0wqtIUahvNbiW5MncyVLUJ", this);
        BugBattle.initWithToken("9KaIxwf6XU0wqtIUahvNbiW5MncyVLUJ", BugBattleActivationMethod.SHAKE, this);
        BugBattle.getInstance().setCustomerEmail("nklas@bbros.dev");


        BugBattle.getInstance().logNetwork("https://google.com", RequestType.GET, 200, 200, new JSONObject(), new JSONObject());

        CustomAction.CustomActionCallback customActionCallback = new CustomAction.CustomActionCallback() {
            @Override
            public void invoke() {
                System.err.println("HEY THIS WORKS RIGHT?");
            }
        };
        CustomAction customAction = new CustomAction("callMe", customActionCallback);
        BugBattle.getInstance().registerCustomAction(customAction);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("HEY", "YOU ");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BugBattle.getInstance().attachCustomData(jsonObject);
        BugBattle.getInstance().setUserAttribute("hey", "you");

        JSONObject jsonObject2 = new JSONObject();
        try {
            jsonObject2.put("OVERRIDE", "YOU ");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        BugBattle.getInstance().attachData(jsonObject2);
    }
}

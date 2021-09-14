package bugbattle.io.bugbattle_android_sdk;

import android.app.Application;

import org.json.JSONException;
import org.json.JSONObject;

import bugbattle.io.bugbattle.BugBattle;
import bugbattle.io.bugbattle.BugBattleActivationMethod;
import bugbattle.io.bugbattle.BugWillBeSentCallback;
import bugbattle.io.bugbattle.CustomActionCallback;
import bugbattle.io.bugbattle.RequestType;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        BugBattle.initWithToken("7qnF4SaW8daomwcBLdXAd8ahlIYJtxos", BugBattleActivationMethod.SHAKE,this);
    BugBattle.getInstance().setCustomerName("HEY YOU");
BugBattle.getInstance().setColor("#ff0000");

        BugBattle.getInstance().enableReplays(true);
        // BugBattle.autoConfigure("9KaIxwf6XU0wqtIUahvNbiW5MncyVLUJ", this);


        //BugBattle.getInstance().sendSilentBugReport("hello@bugbattle.io","Decribe the bug report", BugBattle.SEVERITY.LOW);

        BugWillBeSentCallback bugWillBeSentCallback = new BugWillBeSentCallback() {
            @Override
            public void flowInvoced() {
                System.out.println("CALLED?");
            }
        };
        BugBattle.getInstance().setLanguage("fr");
        System.out.println("THIS IS A DEMO LOG");

        //BugBattle.getInstance().setCustomerEmail("NIKLAS@bbros.dev");


        BugBattle.getInstance().setBugWillBeSentCallback(bugWillBeSentCallback);
        BugBattle.getInstance().logNetwork("https://google.com", RequestType.GET, 200, 200, new JSONObject(), new JSONObject());
        CustomActionCallback customActionCallback = new CustomActionCallback() {
            @Override
            public void invoke(String data) {
                System.out.println(data);
                System.err.println("HEY THIS WORKS RIGHT?");
            }
        };

        BugBattle.getInstance().registerCustomAction(customActionCallback);
        BugBattle.getInstance().enablePrivacyPolicy(false);
        BugBattle.getInstance().setPrivacyPolicyUrl("https://google.com");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("HEY", "YOU ");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BugBattle.getInstance().setCustomData("HEY", "YOU");
        BugBattle.getInstance().removeCustomDataForKey("HEY");
        BugBattle.getInstance().appendCustomData(jsonObject);
        BugBattle.getInstance().setCustomData("hey", "you");

        JSONObject jsonObject2 = new JSONObject();
        try {
            jsonObject2.put("OVERRIDE", "YOU ");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        BugBattle.getInstance().logEvent("JSON", jsonObject2);

        BugBattle.getInstance().logEvent( "event1");
        BugBattle.getInstance().logEvent( "event2");


        BugBattle.getInstance().attachData(jsonObject2);
    }
}

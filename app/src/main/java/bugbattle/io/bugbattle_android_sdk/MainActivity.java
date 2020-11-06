package bugbattle.io.bugbattle_android_sdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import bugbattle.io.bugbattle.BugBattle;
import bugbattle.io.bugbattle.controller.BugBattleActivationMethod;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BugBattle.initialise("UpGjXX9qLCPd93JF0OHpkbcTVxfER1zN",BugBattleActivationMethod.SHAKE, this.getApplication());
        BugBattle.setApiURL("http://192.168.0.10:9000");
    }
}


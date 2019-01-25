package bugbattle.io.bugbattle_android_sdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import bugbattle.io.bugbattle.BugBattle;
import bugbattle.io.bugbattle.BugBattleActivationMethod;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BugBattle.initialize("randomseed", BugBattleActivationMethod.SHAKE, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BugBattle.resume();
    }
}

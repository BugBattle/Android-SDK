package bugbattle.io.bugbattle_android_sdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import bugbattle.io.bugbattle.BugBattle;
import bugbattle.io.bugbattle.controller.BugBattleActivationMethod;
import bugbattle.io.bugbattle.controller.BugBattleNotInitialisedException;

public class MainActivity extends AppCompatActivity {
    private BugBattle bug;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}


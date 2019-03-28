package bugbattle.io.bugbattle_android_sdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import bugbattle.io.bugbattle.BugBattle;
import bugbattle.io.bugbattle.BugBattleActivationMethod;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BugBattle.initialise(getApplication(), "5c41fcd794aaf5331dd4d513",BugBattleActivationMethod.SHAKE);
        BugBattle.setAppBarColor("#ff0000");
    }
}

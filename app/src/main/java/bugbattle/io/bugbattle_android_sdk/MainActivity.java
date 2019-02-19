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
        BugBattle.initialise(getApplication(), "", BugBattleActivationMethod.SHAKE);
        ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BugBattle.startBugReporting();
                }catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
    }
}

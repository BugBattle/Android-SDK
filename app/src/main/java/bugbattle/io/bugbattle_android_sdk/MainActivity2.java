package bugbattle.io.bugbattle_android_sdk;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import bugbattle.io.bugbattle.BugBattle;
import bugbattle.io.bugbattle.controller.BugBattleNotInitialisedException;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Alert")
                .setTitle("Warning");

        AlertDialog alert =builder.create();
        alert.show();

        Button button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BugBattle.startBugReporting();
                } catch (BugBattleNotInitialisedException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
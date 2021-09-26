package gleap.io.bugbattle_android_sdk;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import gleap.io.gleap.Gleap;
import gleap.io.gleap.GleapNotInitialisedException;

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
                    Gleap.getInstance().startBugReporting();

                } catch (GleapNotInitialisedException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
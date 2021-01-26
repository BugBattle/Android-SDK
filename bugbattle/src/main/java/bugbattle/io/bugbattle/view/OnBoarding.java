package bugbattle.io.bugbattle.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import bugbattle.io.bugbattle.R;

public class OnBoarding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_on_boarding);
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException ex) {
            System.out.println(ex);
        }
    }
}
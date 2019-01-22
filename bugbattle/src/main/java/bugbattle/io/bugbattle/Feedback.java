package bugbattle.io.bugbattle;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import bugbattle.io.bugbattle.helper.FeedbackService;
import bugbattle.io.bugbattle.helper.HttpHelper;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Feedback extends AppCompatActivity {
    private ImageView imageView;
    private Button button;
    private View loading;
    private View done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        loading = (View) findViewById(R.id.loading);
        loading.setVisibility(View.INVISIBLE);
        done = (View) findViewById(R.id.done);
        done.setVisibility(View.INVISIBLE);
        button = (Button)findViewById(R.id.btn_send);
        final FeedbackService service = FeedbackService.getInstance();

        EditText email = (EditText)findViewById(R.id.email);
        service.setEmail(email.getText().toString());
        EditText description = (EditText)findViewById(R.id.description);
        service.setDescription(description.getText().toString());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                new HttpHelper().execute(service);
                loading.setVisibility(View.INVISIBLE);
                done.setVisibility(View.VISIBLE);
            }
        });
    }
}

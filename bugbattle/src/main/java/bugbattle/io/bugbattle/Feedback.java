package bugbattle.io.bugbattle;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.w3c.dom.Text;

import bugbattle.io.bugbattle.helper.FeedbackService;
import bugbattle.io.bugbattle.helper.HttpHelper;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Feedback extends AppCompatActivity {
    private Button button;
    private Button btnClose;
    private View loading;
    private View done;
    private View feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        loading = (View) findViewById(R.id.bb_loading);
        loading.setVisibility(View.INVISIBLE);
        done = (View) findViewById(R.id.bb_done_view);
        done.setVisibility(View.INVISIBLE);
        feedback = (View) findViewById(R.id.bb_feedback);
        button = (Button)findViewById(R.id.bb_btnsend);
        btnClose = (Button) findViewById(R.id.bb_btnclose);
        final FeedbackService service = FeedbackService.getInstance();

        EditText email = (EditText)findViewById(R.id.email);
        service.setEmail(email.getText().toString());
        EditText description = (EditText)findViewById(R.id.description);
        service.setDescription(description.getText().toString());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedback.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.VISIBLE);
                try{
                    Integer httpResponse = new HttpHelper().execute(service).get();
                    if(httpResponse == 200) {
                        loading.setVisibility(View.INVISIBLE);
                        done.setVisibility(View.VISIBLE);
                    } else if(httpResponse == 400) {
                        loading.setVisibility(View.INVISIBLE);
                        TextView txt = (TextView)findViewById(R.id.bb_done);
                        txt.setText("Ups, there is an Error.");
                        done.setVisibility(View.VISIBLE);
                    } else {

                    }

                }catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}

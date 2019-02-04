package bugbattle.io.bugbattle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Feedback extends AppCompatActivity implements OnHttpResponseListener {
    private Button button;
    private View loading;
    private View done;
    private View feedback;
    private SharedPreferences pref;
    private ImageView thumbnail;
    private ImageButton backToEdit;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        pref = getApplicationContext().getSharedPreferences("prefs", 0);

        loading = (View) findViewById(R.id.bb_loading_view);
        loading.setVisibility(View.INVISIBLE);
        done = (View) findViewById(R.id.bb_done_view);
        done.setVisibility(View.INVISIBLE);
        feedback = (View) findViewById(R.id.bb_feedback);
        button = (Button) findViewById(R.id.bb_btnsend);
        final FeedbackService service = FeedbackService.getInstance();
        final EditText description = (EditText) findViewById(R.id.description);
        email = (EditText) findViewById(R.id.bb_email);
        //service.setEmail(email.getText().toString());
        if (pref.getString("email", null) != null) {
            email.setText(pref.getString("email", null));
            description.requestFocus();
        }
        backToEdit = (ImageButton) findViewById(R.id.bb_edit_btn);
        final Bitmap thumbnailImage = service.getImage();
        int width = thumbnailImage.getWidth();
        int height = thumbnailImage.getHeight();
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(0.5f, 0.5f);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                thumbnailImage, 0, 0, width, height, matrix, false);
        thumbnail = (ImageView) findViewById(R.id.bb_thumbnail);
        thumbnail.setImageBitmap(resizedBitmap);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedback.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.VISIBLE);
                hideKeyboard(Feedback.this);
                service.setEmail(email.getText().toString());
                service.setDescription(description.getText().toString());
                storeEmail();
                try {
                    new HttpHelper(Feedback.this).execute(service);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });


        backToEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Feedback.this, ImageEditor.class);
                startActivity(intent);
            }
        });
    }


    private void storeEmail() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", this.email.getText().toString()); // Storing string
        editor.commit();
    }


    private static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    public void onTaskComplete(int httpResponse) {
        if(httpResponse == 200) {
            done.setVisibility(View.VISIBLE);
            loading.setVisibility(View.INVISIBLE);

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            finish();
                        }
                    }, 1500);
        }else {

        }
    }
}

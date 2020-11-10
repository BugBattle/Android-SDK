package bugbattle.io.bugbattle.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import bugbattle.io.bugbattle.R;
import bugbattle.io.bugbattle.controller.OnHttpResponseListener;
import bugbattle.io.bugbattle.model.FeedbackModel;
import bugbattle.io.bugbattle.service.HttpHelper;


public class Feedback extends AppCompatActivity implements OnHttpResponseListener {
    private Button sendButton;
    private Button cancleButton;

    private RadioButton priorityLow;
    private RadioButton priorityMedium;
    private RadioButton priorityHigh;

    private Switch privacySwitch;
    private boolean privacyIsToggled = false;

    private View loadingView;
    private View doneSendingView;
    private View errorSendingView;
    private View feedbackView;

    private ImageButton backToEditImageButton;

    private EditText emailEditText;
    private EditText descriptionEditText;

    private TextView policyText;

    private FeedbackModel feedbackModel;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException ex) {
            System.out.println(ex);
        }
        feedbackModel = FeedbackModel.getInstance();

        initComponents();
        setOnClickListener();
        priorityToggle();
        pref = getApplicationContext().getSharedPreferences("prefs", 0);
        if (FeedbackModel.getInstance().getEmail().equals("")) {
            storeEmail(FeedbackModel.getInstance().getEmail());
        }
        loadEmail();

        if(!feedbackModel.isPrivacyEnabled()) {
            policyText.setVisibility(View.GONE);
            privacySwitch.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something on back.
            Intent intent = new Intent(Feedback.this, ImageEditor.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void storeEmail() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", this.emailEditText.getText().toString()); // Storing the email
        editor.apply();
    }

    private void storeEmail(String email) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", email); // Storing the email
        editor.apply();
    }

    private void storeDescription() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("descriptionEditText", this.descriptionEditText.getText().toString()); // Storing the description
        editor.apply();
    }

    private void resetDescription() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("descriptionEditText", ""); // Storing the description
        editor.apply();
    }

    private static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onTaskComplete(int httpResponse) {
        if (httpResponse == 201) {
            doneSendingView.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.INVISIBLE);
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            resetDescription();
                            if (feedbackModel.getGestureDetector() != null) {
                                feedbackModel.getGestureDetector().resume();
                            }
                            finish();

                        }
                    }, 1500);
        } else {
            loadingView.setVisibility(View.INVISIBLE);
            doneSendingView.setVisibility(View.INVISIBLE);
            errorSendingView.setVisibility(View.VISIBLE);
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            errorSendingView.setVisibility(View.INVISIBLE);
                            feedbackView.setVisibility(View.VISIBLE);
                        }
                    }, 1500);
        }
    }

    private void initComponents() {
        loadingView = findViewById(R.id.bb_loading_view);
        loadingView.setVisibility(View.INVISIBLE);
        errorSendingView = findViewById(R.id.bb_done_error);
        errorSendingView.setVisibility(View.INVISIBLE);
        doneSendingView = findViewById(R.id.bb_done_view);
        doneSendingView.setVisibility(View.INVISIBLE);
        feedbackView = findViewById(R.id.bb_feedback);
        sendButton = findViewById(R.id.bb_btnsend);
        cancleButton = findViewById(R.id.bb_btncancle);
        descriptionEditText = findViewById(R.id.description);
        emailEditText = findViewById(R.id.bb_email);
        policyText = findViewById(R.id.policyText);
        policyText.setText(Html.fromHtml(getString(R.string.policy)));
        privacySwitch = findViewById(R.id.bb_privacyswitch);
        if (emailEditText.getText().length() == 0) {
            sendButton.setEnabled(false);
        }
        backToEditImageButton = findViewById(R.id.bb_edit_btn);

        // Prepare thumbnail of screenshot
        ImageView thumbnailImageView = findViewById(R.id.bb_thumbnail);
        final Bitmap thumbnailImage = feedbackModel.getScreenshot();
        int width = thumbnailImage.getWidth();
        int height = thumbnailImage.getHeight();
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(0.5f, 0.5f);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                thumbnailImage, 0, 0, width, height, matrix, false);
        thumbnailImageView.setImageBitmap(resizedBitmap);
    }

    private void setOnClickListener() {
        emailEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (count > 0 || start > 0) {
                    sendButton.setEnabled(true);
                    storeEmail();
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(privacyIsToggled || !feedbackModel.isPrivacyEnabled()) {
                    feedbackView.setVisibility(View.INVISIBLE);
                    loadingView.setVisibility(View.VISIBLE);
                    hideKeyboard(Feedback.this);
                    feedbackModel.setEmail(emailEditText.getText().toString());
                    feedbackModel.setDescription(descriptionEditText.getText().toString());

                    storeEmail();
                    resetDescription();
                    try {
                        new HttpHelper(Feedback.this, getApplicationContext()).execute(feedbackModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        backToEditImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeDescription();
                storeEmail();
                Intent intent = new Intent(Feedback.this, ImageEditor.class);
                startActivity(intent);

                finish();
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
            }
        });

        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeDescription();
                storeEmail();
                Intent intent = new Intent(Feedback.this, ImageEditor.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
            }
        });

        policyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(feedbackModel.getPrivacyUrl()));
                startActivity(browserIntent);
            }
        });

        privacySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                privacyIsToggled = isChecked;
                // do something, the isChecked will be
                // true if the switch is in the On position
            }
        });
    }

    private void priorityToggle() {

        priorityLow = findViewById(R.id.priorityLow);
        priorityMedium = findViewById(R.id.priorityMedium);
        priorityHigh = findViewById(R.id.priorityHigh);


        priorityLow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(priorityLow.isChecked()) {
                    feedbackModel.setSeverity("LOW");
                }
            }
        });

        priorityMedium.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(priorityMedium.isChecked()) {
                    feedbackModel.setSeverity("MEDIUM");
                }
            }
        });
        priorityHigh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(priorityHigh.isChecked()) {
                    feedbackModel.setSeverity("HIGH");
                }
            }
        });
    }

    private void loadEmail() {
        if (pref.getString("email", null) != null && !pref.getString("email", null).equals("")) {
            emailEditText.setText(pref.getString("email", null));
            descriptionEditText.requestFocus();
        }
        if (pref.getString("descriptionEditText", null) != null && !pref.getString("descriptionEditText", null).equals("")) {
            descriptionEditText.setText(pref.getString("descriptionEditText", null));
            descriptionEditText.clearFocus();
        }
    }

}

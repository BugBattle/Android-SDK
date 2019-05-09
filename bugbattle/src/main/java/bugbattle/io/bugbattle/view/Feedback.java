package bugbattle.io.bugbattle.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
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

import bugbattle.io.bugbattle.R;
import bugbattle.io.bugbattle.controller.OnHttpResponseListener;
import bugbattle.io.bugbattle.service.HttpHelper;


public class Feedback extends AppCompatActivity implements OnHttpResponseListener {
    private Button button;
    private Button cancle;
    private View loading;
    private View done;
    private View error;
    private View feedback;
    private SharedPreferences pref;
    private ImageView thumbnail;
    private ImageButton backToEdit;
    private EditText email;
    private EditText description;
    private bugbattle.io.bugbattle.model.Feedback service;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        service = bugbattle.io.bugbattle.model.Feedback.getInstance();
        pref = getApplicationContext().getSharedPreferences("prefs", 0);

        loading = (View) findViewById(R.id.bb_loading_view);
        loading.setVisibility(View.INVISIBLE);
        error = (View) findViewById(R.id.bb_done_error);
        error.setVisibility(View.INVISIBLE);
        done = (View) findViewById(R.id.bb_done_view);
        done.setVisibility(View.INVISIBLE);
        feedback = (View) findViewById(R.id.bb_feedback);
        button = (Button) findViewById(R.id.bb_btnsend);
        cancle = (Button) findViewById(R.id.bb_btncancle);
        final bugbattle.io.bugbattle.model.Feedback service = bugbattle.io.bugbattle.model.Feedback.getInstance();
        View headerView = findViewById(R.id.bb_header_view);
        headerView.setBackgroundColor(Color.parseColor(service.getAppBarColor()));
        description = (EditText) findViewById(R.id.description);
        email = (EditText) findViewById(R.id.bb_email);
        //service.setEmail(email.getText().toString());
        if (pref.getString("email", null) != null && pref.getString("email", null) != "") {
            email.setText(pref.getString("email", null));
            description.requestFocus();
        }
        if (pref.getString("description", null) != null && pref.getString("description", null)  != "") {
            description.setText(pref.getString("description", null));
            description.clearFocus();
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
        email.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if( count > 0 || start > 0) {
                    button.setEnabled(true);
                    storeEmail();
                }
            }
        });
        if(email.getText().length() == 0) {
            button.setEnabled(false);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    feedback.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.VISIBLE);
                    hideKeyboard(Feedback.this);
                    service.setEmail(email.getText().toString());
                    service.setDescription(description.getText().toString());

                    storeEmail();
                    resetDescription();
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
                storeDescription();
                Intent intent = new Intent(Feedback.this, ImageEditor.class);
                startActivity(intent);
                finish();
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetDescription();
                service.getShakeGestureDetector().resume();
                finish();
            }
        });

        radioButton1 = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        if(radioButton1.isChecked()) {
            Drawable background = service.getContext().getResources().getDrawable(R.drawable.left_active);
            background.setColorFilter(Color.parseColor(service.getAppBarColor()), PorterDuff.Mode.SRC_IN);
            radioButton1.setTextColor(Color.WHITE);
            radioButton1.setBackground(background);
        } else {
            GradientDrawable drawable2 = (GradientDrawable)service.getContext().getResources().getDrawable(R.drawable.left_active);
            drawable2.setStroke(((int)(2*Resources.getSystem().getDisplayMetrics().density)), Color.parseColor(service.getAppBarColor()));
            radioButton1.setBackground(drawable2);
            radioButton1.setTextColor(Color.parseColor(service.getAppBarColor()));
        }
        if(radioButton2.isChecked()) {
            Drawable background = service.getContext().getResources().getDrawable(R.drawable.center_active);
            background.setColorFilter(Color.parseColor(service.getAppBarColor()), PorterDuff.Mode.SRC_IN);
            radioButton2.setBackground(background);
            radioButton2.setTextColor(Color.WHITE);
        } else {
            GradientDrawable drawable2 = (GradientDrawable)service.getContext().getResources().getDrawable(R.drawable.center_active);
            drawable2.setStroke(((int)(2*Resources.getSystem().getDisplayMetrics().density)), Color.parseColor(service.getAppBarColor()));

            radioButton2.setBackground(drawable2);
            radioButton2.setTextColor(Color.parseColor(service.getAppBarColor()));
        }

        if(radioButton3.isChecked()) {
            Drawable background = service.getContext().getResources().getDrawable(R.drawable.right_active);
            background.setColorFilter(Color.parseColor(service.getAppBarColor()), PorterDuff.Mode.SRC_IN);
            radioButton3.setBackground(background);
            radioButton3.setTextColor(Color.WHITE);
        }else {
            GradientDrawable drawable2 = (GradientDrawable)service.getContext().getResources().getDrawable(R.drawable.right_active);
            drawable2.setStroke(((int)(2*Resources.getSystem().getDisplayMetrics().density)), Color.parseColor(service.getAppBarColor()));
            radioButton3.setBackground(drawable2);
            radioButton3.setTextColor(Color.parseColor(service.getAppBarColor()));
        }

        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(radioButton1.isChecked()) {
                    service.setSeverity("low");
                    Drawable background = service.getContext().getResources().getDrawable(R.drawable.left_active);
                    background.setColorFilter(Color.parseColor(service.getAppBarColor()), PorterDuff.Mode.SRC_IN);
                    radioButton1.setTextColor(Color.WHITE);
                    radioButton1.setBackground(background);
                } else {
                   GradientDrawable drawable2 = (GradientDrawable)service.getContext().getResources().getDrawable(R.drawable.left_active);
                    drawable2.setStroke(((int)(2*Resources.getSystem().getDisplayMetrics().density)), Color.parseColor(service.getAppBarColor()));
                    radioButton1.setTextColor(Color.parseColor(service.getAppBarColor()));
                    radioButton1.setBackground(drawable2);
                }
            }
        });

        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(radioButton2.isChecked()) {
                    service.setSeverity("medium");
                    Drawable background = service.getContext().getResources().getDrawable(R.drawable.center_active);
                    background.setColorFilter(Color.parseColor(service.getAppBarColor()), PorterDuff.Mode.SRC_IN);
                    radioButton2.setTextColor(Color.WHITE);
                    radioButton2.setBackground(background);
                } else {
                    GradientDrawable drawable2 = (GradientDrawable)service.getContext().getResources().getDrawable(R.drawable.center_active);
                    drawable2.setStroke(((int)(2*Resources.getSystem().getDisplayMetrics().density)), Color.parseColor(service.getAppBarColor()));
                    radioButton2.setTextColor(Color.parseColor(service.getAppBarColor()));
                    radioButton2.setBackground(drawable2);
                }
            }
        });
        radioButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(radioButton3.isChecked()) {
                    service.setSeverity("high");
                    Drawable background = service.getContext().getResources().getDrawable(R.drawable.right_active);
                    background.setColorFilter(Color.parseColor(service.getAppBarColor()), PorterDuff.Mode.SRC_IN);
                    radioButton3.setTextColor(Color.WHITE);
                    radioButton3.setBackground(background);
                } else {
                    GradientDrawable drawable2 = (GradientDrawable)service.getContext().getResources().getDrawable(R.drawable.right_active);
                    drawable2.setStroke(((int)(2*Resources.getSystem().getDisplayMetrics().density)), Color.parseColor(service.getAppBarColor()));
                    radioButton3.setTextColor(Color.parseColor(service.getAppBarColor()));
                    radioButton3.setBackground(drawable2);
                }
            }
        });

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
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
        editor.putString("email", this.email.getText().toString()); // Storing string
        editor.commit();
    }

    private void storeDescription() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("description", this.description.getText().toString()); // Storing string
        editor.commit();
    }

    private void resetDescription(){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("description", ""); // Storing string
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
                            resetDescription();
                            service.getShakeGestureDetector().resume();
                            finish();

                        }
                    }, 1500);
        }else {
            loading.setVisibility(View.INVISIBLE);
            done.setVisibility(View.INVISIBLE);
            error.setVisibility(View.VISIBLE);
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                           error.setVisibility(View.INVISIBLE);
                           feedback.setVisibility(View.VISIBLE);
                        }
                    }, 1500);
        }
    }
}

package bugbattle.io.bugbattle.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import bugbattle.io.bugbattle.R;
import bugbattle.io.bugbattle.model.FeedbackModel;
import bugbattle.io.bugbattle.service.ImageMerger;

public class ImageEditor extends AppCompatActivity {
    private ImageView imageView;
    private DrawerView drawerView;
    private Button red;
    private Button violette;
    private Button blue;
    private Button lightBlue;
    private Button green;
    private Button yellow;
    private Button gray;

    private FeedbackModel service;
    //Add Navigation
    private Button next;
    private Button back;
    private Boolean backClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_editor);

        service = FeedbackModel.getInstance();
        if (service.getScreenshot().getWidth() > service.getScreenshot().getHeight()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

        View headerView = findViewById(R.id.bb_header_view);
        headerView.setBackgroundColor(Color.parseColor(service.getAppBarColor()));
        imageView = findViewById(R.id.bb_image);
        if (imageView != null) {
            imageView.setImageBitmap(service.getScreenshot());
        }
        drawerView = findViewById(R.id.bb_drawerview);
        next = findViewById(R.id.bb_next);
        back = findViewById(R.id.bb_close);
        red = findViewById(R.id.bb_redbutton);
        violette = findViewById(R.id.bb_violettebutton);
        blue = findViewById(R.id.bb_bluebutton);
        lightBlue = findViewById(R.id.bb_lightbluebutton);
        green = findViewById(R.id.bb_greenbutton);
        yellow = findViewById(R.id.bb_yellowbutton);
        gray = findViewById(R.id.bb_graybutton);
        setOnClickListener();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something on back.
            AlertDialog alertDialog = new AlertDialog.Builder(ImageEditor.this).create();
            alertDialog.setTitle("Back to the app");
            alertDialog.setMessage("Do you want to go back to the app?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            service.getShakeGestureDetector().resume();
                            service.setScreenshot(null);
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("prefs", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("description", ""); // Storing string
                            editor.apply();
                            finish();
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private enum SELECTED_COLOR {
        RED, VIOLETTE, BLUE, LIGHTBLUE, GREEN, YELLOW, GRAY
    }

    private void setBackgroundColor(SELECTED_COLOR selectedColor) {
        red.setBackground(getResources().getDrawable(R.drawable.roundbutton_red));
        violette.setBackground(getResources().getDrawable(R.drawable.roundbutton_violette));
        blue.setBackground(getResources().getDrawable(R.drawable.roundbutton_blue));
        lightBlue.setBackground(getResources().getDrawable(R.drawable.roundbutton_lightblue));
        green.setBackground(getResources().getDrawable(R.drawable.roundbutton_green));
        yellow.setBackground(getResources().getDrawable(R.drawable.roundbutton_yellow));
        gray.setBackground(getResources().getDrawable(R.drawable.roundbutton_gray));
        if (selectedColor == SELECTED_COLOR.RED) {
            if (drawerView != null) {
                drawerView.setColor(Color.RED);
            }
            red.setBackground(getResources().getDrawable(R.drawable.roundbutton_red_selected));
        }
        if (selectedColor == SELECTED_COLOR.VIOLETTE) {
            if (drawerView != null) {
                drawerView.setColor(Color.rgb(152, 34, 167));
            }
            violette.setBackground(getResources().getDrawable(R.drawable.roundbutton_violette_selected));
        }
        if (selectedColor == SELECTED_COLOR.BLUE) {
            if (drawerView != null) {
                drawerView.setColor(Color.BLUE);
            }
            blue.setBackground(getResources().getDrawable(R.drawable.roundbutton_blue_selected));
        }
        if (selectedColor == SELECTED_COLOR.LIGHTBLUE) {
            if (drawerView != null) {
                drawerView.setColor(Color.rgb(0, 189, 208));
            }
            lightBlue.setBackground(getResources().getDrawable(R.drawable.roundbutton_lightblue_selected));
        }
        if (selectedColor == SELECTED_COLOR.GREEN) {
            if (drawerView != null) {
                drawerView.setColor(Color.rgb(80, 176, 96));
            }
            green.setBackground(getResources().getDrawable(R.drawable.roundbutton_green_selected));
        }
        if (selectedColor == SELECTED_COLOR.YELLOW) {
            if (drawerView != null) {
                drawerView.setColor(Color.rgb(255, 198, 79));
            }
            yellow.setBackground(getResources().getDrawable(R.drawable.roundbutton_yellow_selected));
        }
        if (selectedColor == SELECTED_COLOR.GRAY) {
            if (drawerView != null) {
                drawerView.setColor(Color.rgb(51, 51, 51));
            }
            gray.setBackground(getResources().getDrawable(R.drawable.roundbutton_gray_selected));
        }
    }

    private void setOnClickListener() {
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(SELECTED_COLOR.RED);
            }
        });
        violette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(SELECTED_COLOR.VIOLETTE);
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(SELECTED_COLOR.BLUE);
            }
        });
        lightBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(SELECTED_COLOR.LIGHTBLUE);
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(SELECTED_COLOR.GREEN);
            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(SELECTED_COLOR.YELLOW);
            }
        });
        gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(SELECTED_COLOR.GRAY);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // File result = saveBitmap());
                Intent intent = new Intent(ImageEditor.this, bugbattle.io.bugbattle.view.Feedback.class);
                Bitmap mergedImage = ImageMerger.mergeImages(loadBitmapFromView(imageView), loadBitmapFromView(drawerView));
                service.setScreenshot(mergedImage);
                ImageEditor.this.startActivity(intent);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                if (!backClicked) {
                    backClicked = true;
                    service.setScreenshot(null);
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("prefs", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("description", ""); // Storing string
                    editor.apply();
                    service.getShakeGestureDetector().resume();
                    finish();
                }
            }
        });
    }

    private Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

}

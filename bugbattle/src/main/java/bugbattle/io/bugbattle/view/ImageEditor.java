package bugbattle.io.bugbattle.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import bugbattle.io.bugbattle.R;
import bugbattle.io.bugbattle.controller.LanguageController;
import bugbattle.io.bugbattle.model.FeedbackModel;
import bugbattle.io.bugbattle.service.ImageMerger;
import bugbattle.io.bugbattle.util.BBDetectorUtil;

public class ImageEditor extends AppCompatActivity {
    private ImageView imageView;
    private DrawerView drawerView;
    private Button red;
    private Button blue;
    private Button yellow;
    private FeedbackModel service;

    //Add Navigation
    private Button next;
    private Button back;
    private Boolean backClicked = false;

    //menu
    private ImageButton undo;
    private Button colorWheelRed;
    private Button colorWheelBlue;
    private Button colorWheelYellow;
    private ImageButton closeColorPicker;
    private ImageButton blur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!FeedbackModel.getInstance().getLanguage().equals("")) {
            LanguageController.setLocale(this, FeedbackModel.getInstance().getLanguage());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_editor);
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException ex) {
            System.out.println(ex);
        }
        service = FeedbackModel.getInstance();

        imageView = findViewById(R.id.bb_image);
        Bitmap screenshot = service.getScreenshot();
        if (imageView != null && screenshot != null) {
            if (screenshot.getWidth() > screenshot.getHeight()) {
                imageView.setImageBitmap(downscaleBitmap(screenshot));
            } else {
                imageView.setImageBitmap(screenshot);
            }
        }

        drawerView = findViewById(R.id.bb_drawerview);
        next = findViewById(R.id.bb_next);
        back = findViewById(R.id.bb_close);
        red = findViewById(R.id.bb_redbutton);
        blue = findViewById(R.id.bb_greenbutton);
        yellow = findViewById(R.id.bb_yellowbutton);
        undo = findViewById(R.id.bb_undobutton);
        blur = findViewById(R.id.bb_blurbutton);
        colorWheelRed = findViewById(R.id.bb_color_red);
        colorWheelBlue = findViewById(R.id.bb_color_blue);
        colorWheelYellow = findViewById(R.id.bb_color_yellow);
        closeColorPicker = findViewById(R.id.bb_close_colorpicker);
        setOnClickListener();
    }

    /**
     * Downscale Bitmap to fit landscape format
     *
     * @param bitmap image in
     * @return downscaled image out
     */
    private Bitmap downscaleBitmap(Bitmap bitmap) {
        double width = ((double) bitmap.getWidth() / 1.1);
        double height = ((double) bitmap.getHeight() / 1.1);
        return Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something on back.
            AlertDialog alertDialog = new AlertDialog.Builder(ImageEditor.this).create();
            alertDialog.setTitle(getString(R.string.bb_back_to_app));
            alertDialog.setMessage(getString(R.string.bb_back_to_app_msg));
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            BBDetectorUtil.resumeAllDetectors();
                            FeedbackModel.getInstance().setDisabled(false);
                            service.setScreenshot(null);
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("prefs", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("description", ""); // Storing string
                            editor.apply();
                            finish();
                            dialog.dismiss();
                            if (service.getBugSentCallback() != null) {
                                service.getBugSentCallback().close();
                            }
                        }
                    });
            alertDialog.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private enum SELECTED_COLOR {
        RED, BLUE, YELLOW, BLUR
    }

    private void generateColorPickerWheel(SELECTED_COLOR selectedColor) {
        if (selectedColor == SELECTED_COLOR.YELLOW) {
            colorWheelRed.setBackground(getResources().getDrawable(R.drawable.roundbutton_red));
            colorWheelBlue.setBackground(getResources().getDrawable(R.drawable.roundbutton_blue));
            colorWheelYellow.setBackground(getResources().getDrawable(R.drawable.roundbutton_yellow_selected));

        }
        if (selectedColor == SELECTED_COLOR.BLUE) {
            colorWheelRed.setBackground(getResources().getDrawable(R.drawable.roundbutton_red));
            colorWheelBlue.setBackground(getResources().getDrawable(R.drawable.roundbutton_blue_selected));
            colorWheelYellow.setBackground(getResources().getDrawable(R.drawable.roundbutton_yellow));
        }
        if (selectedColor == SELECTED_COLOR.RED) {
            colorWheelRed.setBackground(getResources().getDrawable(R.drawable.roundbutton_red_selected));
            colorWheelBlue.setBackground(getResources().getDrawable(R.drawable.roundbutton_blue));
            colorWheelYellow.setBackground(getResources().getDrawable(R.drawable.roundbutton_yellow));
        }
        if (selectedColor == SELECTED_COLOR.BLUR) {
            colorWheelRed.setBackground(getResources().getDrawable(R.drawable.roundbutton_red));
            colorWheelBlue.setBackground(getResources().getDrawable(R.drawable.roundbutton_blue));
            colorWheelYellow.setBackground(getResources().getDrawable(R.drawable.roundbutton_yellow));
        }
    }

    private void setBackgroundColor(SELECTED_COLOR selectedColor) {
        red.setBackground(getResources().getDrawable(R.drawable.roundbutton_red));
        blue.setBackground(getResources().getDrawable(R.drawable.roundbutton_blue));
        yellow.setBackground(getResources().getDrawable(R.drawable.roundbutton_yellow));
        if (selectedColor == SELECTED_COLOR.RED) {
            if (drawerView != null) {
                drawerView.setDrawWidth(15);
                blur.setImageResource(R.drawable.bluricon);
                drawerView.setColor(Color.rgb(254, 123, 140));
            }
            red.setBackground(getResources().getDrawable(R.drawable.roundbutton_red_selected));
        }
        if (selectedColor == SELECTED_COLOR.BLUE) {
            if (drawerView != null) {
                drawerView.setDrawWidth(15);
                blur.setImageResource(R.drawable.bluricon);
                drawerView.setColor(Color.rgb(112, 185, 218));
            }
            blue.setBackground(getResources().getDrawable(R.drawable.roundbutton_blue_selected));
        }
        if (selectedColor == SELECTED_COLOR.YELLOW) {
            if (drawerView != null) {
                drawerView.setDrawWidth(15);
                blur.setImageResource(R.drawable.bluricon);
                drawerView.setColor(Color.rgb(236, 216, 83));
            }
            yellow.setBackground(getResources().getDrawable(R.drawable.roundbutton_yellow_selected));
        }
        if (selectedColor == SELECTED_COLOR.BLUR) {
            if (drawerView != null) {
                drawerView.setDrawWidth(50);
                blur.setImageResource(R.drawable.bluriconactive);
                drawerView.setColor(Color.rgb(0, 0, 0));
            }
        }
        generateColorPickerWheel(selectedColor);
        closeColorPickerMenu();
    }

    private void openColorPickerMenu() {
        View colorPickerView = findViewById(R.id.bb_colorpicker);
        colorPickerView.setVisibility(View.VISIBLE);
        View overviewView = findViewById(R.id.bb_overview);
        overviewView.setVisibility(View.GONE);
    }

    private void closeColorPickerMenu() {
        View colorPickerView = findViewById(R.id.bb_colorpicker);
        colorPickerView.setVisibility(View.GONE);
        View overviewView = findViewById(R.id.bb_overview);
        overviewView.setVisibility(View.VISIBLE);
    }

    private void setOnClickListener() {
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(SELECTED_COLOR.RED);
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(SELECTED_COLOR.BLUE);
            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(SELECTED_COLOR.YELLOW);
            }
        });

        blur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(SELECTED_COLOR.BLUR);
                generateColorPickerWheel(SELECTED_COLOR.BLUR);

            }
        });
        colorWheelRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPickerMenu();
            }
        });
        colorWheelBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPickerMenu();
            }
        });
        colorWheelYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPickerMenu();
            }
        });
        closeColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeColorPickerMenu();
            }
        });
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerView != null) {
                    drawerView.undoLastStep();
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFeedbackScreen();
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
                    BBDetectorUtil.resumeAllDetectors();

                    FeedbackModel.getInstance().setDisabled(false);
                    finish();
                    overridePendingTransition(R.anim.slide_down_revert, R.anim.slide_up_revert);
                    if (service.getBugSentCallback() != null) {
                        service.getBugSentCallback().close();
                    }
                }
            }
        });
    }

    private void goToFeedbackScreen() {
        Intent intent = new Intent(ImageEditor.this, bugbattle.io.bugbattle.view.Feedback.class);
        Bitmap mergedImage = ImageMerger.mergeImages(loadBitmapFromView(imageView), loadBitmapFromView(drawerView));
        service.setScreenshot(mergedImage);
        ImageEditor.this.startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }

    private Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }
}

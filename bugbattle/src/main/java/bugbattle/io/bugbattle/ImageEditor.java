package bugbattle.io.bugbattle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
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
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.List;

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

    private FeedbackService service;
    //Add Navigation
    private Button next;
    private Intent nextIntent;
    private Button back;
    private Boolean backClicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_image_editor);
        View headerView = (View)findViewById(R.id.bb_header_view);
        service = FeedbackService.getInstance();

        if(service.getImage().getWidth() > service.getImage().getHeight()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        headerView.setBackgroundColor(Color.parseColor(service.getAppBarColor()));
        imageView = (ImageView) findViewById(R.id.bb_image);
        drawerView = (DrawerView) findViewById(R.id.bb_drawerview);

        //navigation
        next = (Button) findViewById(R.id.bb_next);
        nextIntent = new Intent(this, Feedback.class);
        back = (Button) findViewById(R.id.bb_close);
       // backIntent =
        if(imageView != null) {
            imageView.setImageBitmap(service.getImage());
        }

        initBtn();
    }
     @Override
     public boolean onKeyDown(int keyCode, KeyEvent event)  {
         if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
             // do something on back.
             service.getShakeGestureDetector().resume();
             SharedPreferences pref = getApplicationContext().getSharedPreferences("prefs", 0);
             SharedPreferences.Editor editor = pref.edit();
             editor.putString("description", ""); // Storing string
             editor.commit();
             finish();
             return true;
         }

         return super.onKeyDown(keyCode, event);
     }

    private void initBtn() {
        red = (Button) findViewById(R.id.bb_redbutton);
        violette = (Button) findViewById(R.id.bb_violettebutton);
        blue = (Button) findViewById(R.id.bb_bluebutton);
        lightBlue = (Button) findViewById(R.id.bb_lightbluebutton);
        green = (Button) findViewById(R.id.bb_greenbutton);
        yellow = (Button) findViewById(R.id.bb_yellowbutton);
        gray = (Button) findViewById(R.id.bb_graybutton);

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerView != null) {
                    drawerView.setColor(Color.RED);
                }
                red.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_red_selected));
                violette.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_violette));
                blue.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_blue));
                lightBlue.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_lightblue));
                green.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_green));
                yellow.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_yellow));
                gray.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_gray));
            }
        });
        violette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerView != null) {
                    drawerView.setColor(Color.rgb(152,34,167));
                }
                violette.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_violette_selected));
                red.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_red));
                blue.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_blue));
                lightBlue.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_lightblue));
                green.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_green));
                yellow.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_yellow));
                gray.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_gray));
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerView != null) {
                    drawerView.setColor(Color.BLUE);
                }

                red.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_red));
                violette.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_violette));
                blue.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_blue_selected));
                lightBlue.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_lightblue));
                green.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_green));
                yellow.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_yellow));
                gray.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_gray));
            }
        });
        lightBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerView != null) {
                    drawerView.setColor(Color.rgb(0,189,208));
                }
                red.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_red));
                violette.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_violette));
                blue.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_blue));
                lightBlue.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_lightblue_selected));
                green.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_green));
                yellow.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_yellow));
                gray.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_gray));
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerView != null) {
                    drawerView.setColor(Color.rgb(80,176,96));
                }
                red.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_red));
                violette.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_violette));
                blue.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_blue));
                lightBlue.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_lightblue));
                green.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_green_selected));
                yellow.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_yellow));
                gray.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_gray));
            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerView != null) {
                    drawerView.setColor(Color.rgb(255,198,79));
                }
                red.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_red));
                violette.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_violette));
                blue.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_blue));
                lightBlue.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_lightblue));
                green.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_green));
                yellow.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_yellow_selected));
                gray.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_gray));
            }
        });
        gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerView != null) {
                    drawerView.setColor(Color.rgb(51,51,51));
                }
                red.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_red));
                violette.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_violette));
                blue.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_blue));
                lightBlue.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_lightblue));
                green.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_green));
                yellow.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_yellow));
                gray.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundbutton_gray_selected));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // File result = saveBitmap());
                Intent intent = new Intent(ImageEditor.this, Feedback.class);
                Bitmap mergedImage = ImageProcessing.mergeImages(loadBitmapFromView(imageView), loadBitmapFromView(drawerView));
                service.setImage(mergedImage);
                ImageEditor.this.startActivity(intent);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
             if(!backClicked) {
                 backClicked = true;
                 SharedPreferences pref = getApplicationContext().getSharedPreferences("prefs", 0);
                 SharedPreferences.Editor editor = pref.edit();
                 editor.putString("description", ""); // Storing string
                 editor.commit();
                 service.getShakeGestureDetector().resume();
                 finish();
             }
            }
        });

    }


    private Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap( v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

}

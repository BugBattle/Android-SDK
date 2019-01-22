package bugbattle.io.bugbattle;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import bugbattle.io.bugbattle.Views.DrawerView;
import bugbattle.io.bugbattle.helper.FeedbackService;
import bugbattle.io.bugbattle.helper.FileStorage;
import bugbattle.io.bugbattle.helper.ImageProcessing;
import bugbattle.io.bugbattle.helper.ScreenshotTaker;

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
    private ImageButton next;
    private Intent nextIntent;
    private ImageButton back;
    private Intent backIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        setContentView(R.layout.activity_image_editor);
        service = FeedbackService.getInstance();
        imageView = (ImageView) findViewById(R.id.image);
        drawerView = (DrawerView) findViewById(R.id.drawerview);

        //navigation
        next = (ImageButton) findViewById(R.id.next);
        nextIntent = new Intent(this, Feedback.class);
        back = (ImageButton) findViewById(R.id.close);
       // backIntent =
        if(imageView != null) {
            imageView.setImageBitmap(service.getImage());
        }

        initBtn();
        System.out.println(getLauncherActivityName());
    }
    private String getLauncherActivityName(){
        String activityName = "";
        final PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(getPackageName());
        List<ResolveInfo> activityList = pm.queryIntentActivities(intent,0);

        if(activityList != null){
            activityName = activityList.get(0).activityInfo.name;
        }
        return activityName;
    }
    private void initBtn() {
        red = (Button) findViewById(R.id.redbutton);
        violette = (Button) findViewById(R.id.violettebutton);
        blue = (Button) findViewById(R.id.bluebutton);
        lightBlue = (Button) findViewById(R.id.lightbluebutton);
        green = (Button) findViewById(R.id.greenbutton);
        yellow = (Button) findViewById(R.id.yellowbutton);
        gray = (Button) findViewById(R.id.graybutton);

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerView != null) {
                    drawerView.setColor(Color.RED);
                }
            }
        });
        violette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerView != null) {
                    drawerView.setColor(Color.rgb(152,34,167));
                }
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerView != null) {
                    drawerView.setColor(Color.BLUE);
                }
            }
        });
        lightBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerView != null) {
                    drawerView.setColor(Color.rgb(0,189,208));
                }
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerView != null) {
                    drawerView.setColor(Color.rgb(80,176,96));
                }
            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerView != null) {
                    drawerView.setColor(Color.rgb(255,198,79));
                }
            }
        });
        gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerView != null) {
                    drawerView.setColor(Color.rgb(51,51,51));
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(drawerView.getWidth());
                System.out.println(drawerView.getHeight());
               // File result = saveBitmap());
                Intent intent = new Intent(ImageEditor.this, Feedback.class);
                Bitmap mergedImage = ImageProcessing.mergeImages(loadBitmapFromView(imageView), loadBitmapFromView(drawerView));
                service.setImage(mergedImage);
                ImageEditor.this.startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

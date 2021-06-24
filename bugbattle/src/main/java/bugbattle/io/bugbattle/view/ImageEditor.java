package bugbattle.io.bugbattle.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import bugbattle.io.bugbattle.R;
import bugbattle.io.bugbattle.model.BugBattleBug;
import bugbattle.io.bugbattle.service.ImageMerger;

public class ImageEditor {
    private ImageView imageView;
    private DrawerView drawerView;
    private Button red;
    private Button blue;
    private Button yellow;
    private BugBattleBug service;

    //menu
    private ImageButton undo;
    private Button colorWheelRed;
    private Button colorWheelBlue;
    private Button colorWheelYellow;
    private ImageButton closeColorPicker;
    private ImageButton blur;

    private AppCompatActivity context;

    public ImageEditor(AppCompatActivity context) {
        this.context = context;
    }

    public void init() {


        service = BugBattleBug.getInstance();

        imageView = context.findViewById(R.id.bb_image);
        Bitmap screenshot = service.getScreenshot();

        if (imageView != null && screenshot != null) {
            if (screenshot.getWidth() > screenshot.getHeight()) {
                imageView.setImageBitmap(downscaleBitmap(screenshot));
            } else {
                imageView.setImageBitmap(screenshot);
            }
        }

        drawerView = context.findViewById(R.id.bb_drawerview);

        red = context.findViewById(R.id.bb_redbutton);
        blue = context.findViewById(R.id.bb_greenbutton);
        yellow = context.findViewById(R.id.bb_yellowbutton);
        undo = context.findViewById(R.id.bb_undobutton);
        blur = context.findViewById(R.id.bb_blurbutton);
        colorWheelRed = context.findViewById(R.id.bb_color_red);
        colorWheelBlue = context.findViewById(R.id.bb_color_blue);
        colorWheelYellow = context.findViewById(R.id.bb_color_yellow);
        closeColorPicker = context.findViewById(R.id.bb_close_colorpicker);
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

    private enum SELECTED_COLOR {
        RED, BLUE, YELLOW, BLUR
    }

    private void generateColorPickerWheel(SELECTED_COLOR selectedColor) {
        if (selectedColor == SELECTED_COLOR.YELLOW) {
            colorWheelRed.setBackground(context.getResources().getDrawable(R.drawable.roundbutton_red));
            colorWheelBlue.setBackground(context.getResources().getDrawable(R.drawable.roundbutton_blue));
            colorWheelYellow.setBackground(context.getResources().getDrawable(R.drawable.roundbutton_yellow_selected));

        }
        if (selectedColor == SELECTED_COLOR.BLUE) {
            colorWheelRed.setBackground(context.getResources().getDrawable(R.drawable.roundbutton_red));
            colorWheelBlue.setBackground(context.getResources().getDrawable(R.drawable.roundbutton_blue_selected));
            colorWheelYellow.setBackground(context.getResources().getDrawable(R.drawable.roundbutton_yellow));
        }
        if (selectedColor == SELECTED_COLOR.RED) {
            colorWheelRed.setBackground(context.getResources().getDrawable(R.drawable.roundbutton_red_selected));
            colorWheelBlue.setBackground(context.getResources().getDrawable(R.drawable.roundbutton_blue));
            colorWheelYellow.setBackground(context.getResources().getDrawable(R.drawable.roundbutton_yellow));
        }
        if (selectedColor == SELECTED_COLOR.BLUR) {
            colorWheelRed.setBackground(context.getResources().getDrawable(R.drawable.roundbutton_red));
            colorWheelBlue.setBackground(context.getResources().getDrawable(R.drawable.roundbutton_blue));
            colorWheelYellow.setBackground(context.getResources().getDrawable(R.drawable.roundbutton_yellow));
        }
    }

    private void setBackgroundColor(SELECTED_COLOR selectedColor) {
        red.setBackground(context.getResources().getDrawable(R.drawable.roundbutton_red));
        blue.setBackground(context.getResources().getDrawable(R.drawable.roundbutton_blue));
        yellow.setBackground(context.getResources().getDrawable(R.drawable.roundbutton_yellow));
        if (selectedColor == SELECTED_COLOR.RED) {
            if (drawerView != null) {
                drawerView.setDrawWidth(15);
                blur.setImageResource(R.drawable.bluricon);
                drawerView.setColor(Color.rgb(254, 123, 140));
            }
            red.setBackground(context.getResources().getDrawable(R.drawable.roundbutton_red_selected));
        }
        if (selectedColor == SELECTED_COLOR.BLUE) {
            if (drawerView != null) {
                drawerView.setDrawWidth(15);
                blur.setImageResource(R.drawable.bluricon);
                drawerView.setColor(Color.rgb(112, 185, 218));
            }
            blue.setBackground(context.getResources().getDrawable(R.drawable.roundbutton_blue_selected));
        }
        if (selectedColor == SELECTED_COLOR.YELLOW) {
            if (drawerView != null) {
                drawerView.setDrawWidth(15);
                blur.setImageResource(R.drawable.bluricon);
                drawerView.setColor(Color.rgb(236, 216, 83));
            }
            yellow.setBackground(context.getResources().getDrawable(R.drawable.roundbutton_yellow_selected));
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
        View colorPickerView = context.findViewById(R.id.bb_colorpicker);
        colorPickerView.setVisibility(View.VISIBLE);
        View overviewView = context.findViewById(R.id.bb_overview);
        overviewView.setVisibility(View.GONE);
    }

    private void closeColorPickerMenu() {
        View colorPickerView = context.findViewById(R.id.bb_colorpicker);
        colorPickerView.setVisibility(View.GONE);
        View overviewView = context.findViewById(R.id.bb_overview);
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


    }

    public Bitmap getEditedImage() {
        Bitmap mergedImage = ImageMerger.mergeImages(loadBitmapFromView(imageView), loadBitmapFromView(drawerView));
        return mergedImage;
    }

    private Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }
}

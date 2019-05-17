package bugbattle.io.bugbattle.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

class DrawerView extends View {
    // setup initial color
    private int paintColor = Color.RED;
    // defines paint and canvas
    private List<Paint> drawPaint = new LinkedList<>();
    private Paint tmpPaint;
    // Store circles to draw each time the user touches down
    private List<Path> paths = new LinkedList<>();
    private Path tmpPath;


    public DrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupPaint(); // same as before

    }

    public void setColor(int color) {
        paintColor = color;
        tmpPaint = generatePaint(color);
    }

    public Paint generatePaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE); // change to fill
        return paint;
    }

    // Setup paint with color and stroke styles
    private void setupPaint() {
        // drawPaint.add(generatePaint(paintColor));
        tmpPaint = generatePaint(paintColor);
    }

    // Draw each circle onto the view
    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), drawPaint.get(i));
        }
    }

    // Append new circle each time user presses on screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();
        // Checks for the event that occurs
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Starts a new line in the path
                tmpPath = new Path();
                tmpPath.moveTo(pointX, pointY);
                break;
            case MotionEvent.ACTION_MOVE:
                // Draws line between last point and this point
                tmpPath.lineTo(pointX, pointY);
                drawPaint.add(tmpPaint);
                paths.add(tmpPath);

                break;
            default:
                return false;
        }

        postInvalidate(); // Indicate view should be redrawn
        return true; // Indicate we've consumed the touch
    }


}
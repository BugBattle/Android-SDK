package bugbattle.io.bugbattle.service;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Detects the shake gesture of the phone
 */
public class ShakeGestureDetector implements SensorEventListener  {
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    private long mShakeTimestamp;
    private int mShakeCount;

    private ScreenshotTaker screenshotTaker;
    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    public Context activity;

    /**
     * called when a shake gesture is detected
     * @param mainActivity context is needed to access the sensor
     */
    public ShakeGestureDetector(Context mainActivity) {
        activity = mainActivity;
        //Init Sensors
        mSensorManager = (SensorManager) mainActivity.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        mSensorManager.registerListener(this, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);

        //init screenshot taker
        screenshotTaker = new ScreenshotTaker();
    }

    public void resume() {
        mSensorManager.registerListener(this, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    public void pause() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
            double gForce = Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }

                // reset the shake count after 3 seconds of no shakes
                if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    mShakeCount = 0;
                }

                mShakeTimestamp = now;
                mShakeCount++;
                try {
                    screenshotTaker.takeScreenshot();
                    pause();
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

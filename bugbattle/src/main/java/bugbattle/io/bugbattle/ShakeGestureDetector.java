package bugbattle.io.bugbattle;

import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Detects the shake gesture of the phone
 */
class ShakeGestureDetector extends BBDetector implements SensorEventListener {
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.5F;
    private static final int SHAKE_SLOP_TIME_MS = 600;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;
    private long mShakeTimestamp;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    public ShakeGestureDetector(Application application) {
        super(application);
    }

    @Override
    public void initialize() {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void resume() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void pause() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float gX = sensorEvent.values[0] / SensorManager.GRAVITY_EARTH;
        float gY = sensorEvent.values[1] / SensorManager.GRAVITY_EARTH;
        float gZ = sensorEvent.values[2] / SensorManager.GRAVITY_EARTH;

        // gForce will be close to 1 when there is no movement.
        double gForce = Math.sqrt(gX * gX + gY * gY + gZ * gZ);
        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            final long now = System.currentTimeMillis();
            // ignore shake events too close to each other (500ms)
            if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                return;
            }

            mShakeTimestamp = now;
            try {
                if (!BugBattleBug.getInstance().isDisabled()) {
                    this.takeScreenshot();
                    pause();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}

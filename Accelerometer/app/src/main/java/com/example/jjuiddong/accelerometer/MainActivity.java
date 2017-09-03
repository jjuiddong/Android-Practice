package com.example.jjuiddong.accelerometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mAccelX = 0;
    private float mAccelY = 0;
    private float mAccelZ = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);

        setContentView(R.layout.activity_main);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccelX = event.values[0];
            mAccelY = event.values[1];
            mAccelZ = event.values[2];

            TextView text;
            text = (TextView)findViewById(R.id.textView);
            text.setText("accel x = "+mAccelX);
            text = (TextView)findViewById(R.id.textView2);
            text.setText("accel y = "+mAccelY);
            text = (TextView)findViewById(R.id.textView3);
            text.setText("accel z = "+mAccelZ);

            double norm = mAccelX*mAccelX + mAccelY*mAccelY + mAccelZ*mAccelZ;
            text = (TextView)findViewById(R.id.textView4);
            text.setText("norm z = "+Math.sqrt(norm));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }
}

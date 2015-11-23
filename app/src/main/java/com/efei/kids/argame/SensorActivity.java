package com.efei.kids.argame;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class SensorActivity extends ActionBarActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mGravitySensor;
    private Sensor mAccelerometerSensor;
    private Sensor mRotationVectorSensor;

    private TextView gravity_x_tv;
    private TextView gravity_y_tv;
    private TextView gravity_z_tv;

    private TextView accelerometer_x_tv;
    private TextView accelerometer_y_tv;
    private TextView accelerometer_z_tv;

    private TextView rotation_x_tv;
    private TextView rotation_y_tv;
    private TextView rotation_z_tv;
    private TextView rotation_theta_tv;

    private TextView euler_1_tv;
    private TextView euler_2_tv;
    private TextView euler_3_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        /*
        gravity_x_tv = (TextView) findViewById(R.id.gravity_x_tv);
        gravity_y_tv = (TextView) findViewById(R.id.gravity_y_tv);
        gravity_z_tv = (TextView) findViewById(R.id.gravity_z_tv);

        accelerometer_x_tv = (TextView) findViewById(R.id.accelerometer_x_tv);
        accelerometer_y_tv = (TextView) findViewById(R.id.accelerometer_y_tv);
        accelerometer_z_tv = (TextView) findViewById(R.id.accelerometer_z_tv);
        */

        rotation_x_tv = (TextView) findViewById(R.id.rotation_x_tv);
        rotation_y_tv = (TextView) findViewById(R.id.rotation_y_tv);
        rotation_z_tv = (TextView) findViewById(R.id.rotation_z_tv);
        rotation_theta_tv = (TextView) findViewById(R.id.rotation_theta_tv);

        euler_1_tv = (TextView) findViewById(R.id.euler_1_tv);
        euler_2_tv = (TextView) findViewById(R.id.euler_2_tv);
        euler_3_tv = (TextView) findViewById(R.id.euler_3_tv);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // mGravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        // mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);


        // mSensorManager.registerListener(this, mGravitySensor, 100000);
        // mSensorManager.registerListener(this, mAccelerometerSensor, 100000);
        mSensorManager.registerListener(this, mRotationVectorSensor, 100000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sensor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        /*
        if (sensorEvent.sensor == mGravitySensor) {
            gravity_x_tv.setText(String.valueOf(Math.round(sensorEvent.values[0] * 100) / 100.0));
            gravity_y_tv.setText(String.valueOf(Math.round(sensorEvent.values[1] * 100) / 100.0));
            gravity_z_tv.setText(String.valueOf(Math.round(sensorEvent.values[2] * 100) / 100.0));
        } else if (sensorEvent.sensor == mAccelerometerSensor) {
            accelerometer_x_tv.setText(String.valueOf(Math.round(sensorEvent.values[0] * 100) / 100.0));
            accelerometer_y_tv.setText(String.valueOf(Math.round(sensorEvent.values[1] * 100) / 100.0));
            accelerometer_z_tv.setText(String.valueOf(Math.round(sensorEvent.values[2] * 100) / 100.0));
        } else if (sensorEvent.sensor == mRotationVectorSensor) {*/
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        float magnitude = (float) Math.sqrt(x * x + y * y + z * z);
        float theta = (float)(Math.asin(magnitude) * 2 / 3.14 * 180);
        rotation_x_tv.setText(String.valueOf(Math.round(x / magnitude * 100) / 100.0));
        rotation_y_tv.setText(String.valueOf(Math.round(y / magnitude * 100) / 100.0));
        rotation_z_tv.setText(String.valueOf(Math.round(z / magnitude * 100) / 100.0));
        rotation_theta_tv.setText(String.valueOf(Math.round(theta * 100) / 100.0));


        float[] angles = new float[3];
        calculateAngles(angles, sensorEvent.values);

        euler_1_tv.setText(String.valueOf(Math.round(angles[0] * 100) / 100.0));
        euler_2_tv.setText(String.valueOf(Math.round(angles[1] * 100) / 100.0));
        euler_3_tv.setText(String.valueOf(Math.round(angles[2] * 100) / 100.0));

        //}
    }

    private float[] rMatrix = new float[9];
    /**
     * @param result the array of Euler angles in the order: yaw, roll, pitch
     * @param rVector the rotation vector
     */
    public void calculateAngles(float[] result, float[] rVector){
        //caculate rotation matrix from rotation vector first
        SensorManager.getRotationMatrixFromVector(rMatrix, rVector);

        //calculate Euler angles now
        SensorManager.getOrientation(rMatrix, result);

        //The results are in radians, need to convert it to degrees
        convertToDegrees(result);
    }

    private void convertToDegrees(float[] vector){
        for (int i = 0; i < vector.length; i++){
            vector[i] = Math.round(Math.toDegrees(vector[i]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

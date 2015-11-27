package aengelus.apps.sensoration;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView sensorText;
    TextView minView;
    TextView maxView;
    SensorManager sensorManager;
    Sensor accelerometer;
    Sensor pressure;
    double max = 0;
    double min = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This will instantiate the textview field from the view to the sensorText variable
        sensorText = (TextView) findViewById(R.id.sensorTextView);
        minView = (TextView) findViewById(R.id.minView);
        maxView = (TextView) findViewById(R.id.maxView);

        // Instatiating the sensor manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // The sensor manager is there to look what kind of sensors are available on the device
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // Gives access to the accelerometer
        // If you try to talk to a sensor which is not available the return value is null
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // this is no SensorEventListener, so we have to implements the SensorEventListener
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // This will stop the sensor when the phone is onPause
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // http://developer.android.com/guide/topics/sensors/sensors_motion.html
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        double tmp = calcVector(x,y,z);

        if(tmp <= min){
            min = tmp;
        }
        if(tmp >= max){
            max = tmp;
        }

        //sensorText.setText("Value is: "+event.values[0]);
        //sensorText.setText("Value: " + tmp);
        minView.setText("Min: "+min);
        maxView.setText("Max: "+max);


        //if(event.sensor.getType() == Sensor.TYPE_PRESSURE){
            sensorText.setText("Oh my pressure: " + event.values[0]);
        //}

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private double calcVector(float x, float y, float z){
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z,2));
    }
}

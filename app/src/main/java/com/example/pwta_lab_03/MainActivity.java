package com.example.pwta_lab_03;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView testText = (TextView) findViewById(R.id.testText);
        testText.setText("");
        String temp = "Sensors: ";
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensorMagnetic = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(sensorMagnetic != null) temp += "MagneticON; ";
        Sensor gravSensor = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if(gravSensor != null) temp += "GravityOn;";
        Sensor accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accSensor != null) temp += "AcceleratorOn;";
        testText.setText(temp);
    }
}

class ObserwarotSensorow implements SensorEventListener
{
    float[] gravity=null;
    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()){
            case Sensor.TYPE_GRAVITY:
                gravity = event.values.clone(); //klonujemy dane z sensora
                //TODO
            case Sensor.TYPE_ACCELEROMETER:
                //TODO
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                //TODO
                break;
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {

    }
}
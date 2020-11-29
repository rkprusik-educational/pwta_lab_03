package com.example.pwta_lab_03;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
{
    private SensorManager _sm;
    private SensorObserver _observer;
    private Sensor _accSensor;

    private Timer _timer;
    private float[] _lastValues = null;
    private float[] _values = null;
    private TextView _outputTV;
    private TextView _shakeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _shakeTV = (TextView) findViewById(R.id.shakeTV);
        _outputTV = (TextView) findViewById(R.id.outputTV);
        InitializeAccelerationSensor();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (_accSensor != null)
        {
            _sm.unregisterListener(_observer);
//            StopShakeCheckTimer();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (_accSensor != null)
        {
            _sm.registerListener(_observer, _accSensor, SensorManager.SENSOR_DELAY_GAME);
//            StartShakeCheckTimer();
        }
    }

    public void UpdateSensorValues(float[] values)
    {
        _lastValues = _values;
        _values = values;
        if(_lastValues != null && AreValuesChanged())
        {
//            StopShakeCheckTimer();
            _shakeTV.setText("Shakes!");
        }
        else
        {
//            StartShakeCheckTimer();
        }
        String tempString = "";
        for(int i = 0; i < values.length; i++)
        {
            tempString += "v[" + i + "] = " + _values[i] + "; ";
        }
        _outputTV.setText(tempString);
    }

    private boolean AreValuesChanged()
    {
        if(Math.abs(_values[0] - _lastValues[0]) > 1f ||
            Math.abs(_values[1] - _lastValues[1]) > 1f ||
            Math.abs(_values[2] - _lastValues[2]) > 1f )
            return true;
        return false;
    }

    private void InitializeAccelerationSensor()
    {
        _sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        _accSensor = _sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        _observer = new SensorObserver();
        _observer.parentActivity = this;
    }

    private void StopShakeCheckTimer()
    {
        _timer.cancel();
    }

    private void StartShakeCheckTimer()
    {
        _timer = new Timer();
        _timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                _shakeTV.setText(("NoShake"));
            }
        }, 5*1000, 5*1000); // 0 - time before first execution, 10*1000 - repeating of all subsequent executions

    }

    private void TestSensors()
    {
        TextView testText = (TextView) findViewById(R.id.shakeTV);
        testText.setText("");
        String temp = "Sensors: ";
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        Sensor sensorMagnetic = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(sensorMagnetic != null) temp += "MagneticON; ";

        Sensor gyroSensor = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(gyroSensor != null) temp += "GyroON; ";

        Sensor gravSensor = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if(gravSensor != null) temp += "GravityOn; ";

        Sensor accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accSensor != null) temp += "AcceleratorOn; ";

        testText.setText(temp);
    }
}

class SensorObserver implements SensorEventListener
{
    MainActivity parentActivity;

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            if(parentActivity != null)
                parentActivity.UpdateSensorValues(event.values.clone());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {
        Log.println(Log.DEBUG, "Debug", "OnACC: " + i);
    }

}
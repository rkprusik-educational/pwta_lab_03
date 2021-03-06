package com.example.pwta_lab_03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.sql.Time;
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
    private TextView _faceX;
    private TextView _faceY;
    private TextView _faceZ;

    private float time = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _shakeTV = (TextView) findViewById(R.id.shakeTV);
        _outputTV = (TextView) findViewById(R.id.outputTV);
        _faceX = (TextView) findViewById(R.id.faceXTV);
        _faceY = (TextView) findViewById(R.id.faceYTV);
        _faceZ = (TextView) findViewById(R.id.faceZTV);
        InitializeAccelerationSensor();
    }

    public void openDrawActivity(View view)
    {
        Intent drawAct = new Intent(this, DrawActivity.class);
        MainActivity.this.startActivity(drawAct);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (_accSensor != null)
        {
            _sm.unregisterListener(_observer);
            StopShakeCheckTimer();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (_accSensor != null)
        {
            _sm.registerListener(_observer, _accSensor, SensorManager.SENSOR_DELAY_GAME);
            StartShakeCheckTimer();
        }
    }

    public void UpdateSensorValues(float[] values)
    {
        _lastValues = _values;
        _values = values;
        UpdateFaceValue();
        if(_lastValues != null && AreValuesChanged())
        {
            StopShakeCheckTimer();
            _shakeTV.setText("Shakes!");
        }
        else
        {
            StartShakeCheckTimer();
        }
        String tempString = "";
        for(int i = 0; i < values.length; i++)
        {
            tempString += "v[" + i + "] = " + _values[i] + "; ";
        }
        _outputTV.setText(tempString);
    }

    private void UpdateFaceValue()
    {
        if(_values[0] > 2f) _faceX.setText("TiltedLeft");
        else if(_values[0] < 2f) _faceX.setText("TiltedRight");
        else _faceX.setText("-");

        if(_values[1] > 2f) _faceY.setText("ToMe");
        else if(_values[1] < 2f) _faceY.setText("FromMe");
        else _faceY.setText("-");

        if(_values[2] > 2f) _faceZ.setText("FaceUp");
        else if(_values[2] < 2f) _faceZ.setText("FaceDown");
        else _faceZ.setText("-");
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
//        _timer.cancel();
    }

    private void StartShakeCheckTimer()
    {
//        _timer = new Timer();
//        _timer.scheduleAtFixedRate(new TimerTask()
//        {
//            @Override
//            public void run()
//            {
//                time++;
//                _shakeTV.setText("" + time);
//            }
//        }, 0, 1*1000); // 0 - time before first execution, 10*1000 - repeating of all subsequent executions
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
            {
                parentActivity.UpdateSensorValues(event.values.clone());
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {
        Log.println(Log.DEBUG, "Debug", "OnACC: " + i);
    }

}

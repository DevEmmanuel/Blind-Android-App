package com.perchtech.humraz.blind;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MyTestService extends IntentService implements SensorEventListener {

    public MyTestService() {

        super("test-service");
    }
    SensorManager sensorManager;

    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float gX = x / 9.8f;
        float gY = y / 9.8f;
        float gZ = z / 9.8f;

        float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

        if (gForce>3)
        {
            System.out.println("force" + gForce);
            sensorManager.unregisterListener(MyTestService.this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
            Intent dialogIntent = new Intent(this, menuscreen.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialogIntent);
        }
        }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    protected void onHandleIntent(Intent intent) {

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }
}
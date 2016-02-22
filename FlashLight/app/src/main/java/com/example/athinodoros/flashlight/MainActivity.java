package com.example.athinodoros.flashlight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends Activity implements SensorEventListener {


    private Camera camera;
    private boolean isFlashOn;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private TextView text;
    Camera.Parameters params;
    SensorListener listener;
    TriggerEventListener eventListener;
    boolean stopFlash;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.textF);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //ask for access to magnetic sensor:
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this,lightSensor,SensorManager.SENSOR_DELAY_GAME);
        getCamera();
        stopFlash = false;

    }


    // Get the camera
    @SuppressLint("LongLogTag")
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
            }
        }
    }


    // Turning On flash
    private void turnOnFlash() {
        if (!isFlashOn && !stopFlash) {
            if (camera == null || params == null) {
                return;
            }
            // play sound


            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;



        }

    }


    // Turning Off flash
    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound


            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

            // changing button/switch image

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        stopFlash = false;

    }


    @Override
    protected void onResume() {
        super.onResume();
        stopFlash = false;


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        stopFlash = false;


    }

    @Override
    protected void onPause() {
        super.onPause();
        stopFlash = true;
    }

    @Override
    protected void onStop() {
        stopFlash = true;
        turnOffFlash();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopFlash = true;
        camera.release();
        super.onDestroy();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] < 50){
            turnOnFlash();
        }else{
            turnOffFlash();
        }
        text.setText(String.valueOf(event.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    /*
     * Toggle switch button images
     * changing image states to on / off
     * */







}

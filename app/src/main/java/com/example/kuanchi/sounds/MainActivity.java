package com.example.kuanchi.sounds;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends Activity{

    private SensorManager sensorManager;
    private Sensor magSensor;
    private Sensor accSensor;
    private TextView xView;
    private TextView yView;
    private TextView zView;
    private TextView soundStatus;
    private SensorEventListener listener;
    private float[] orientation;
    final int matrix_size = 16;
    private float[] magVal = new float[matrix_size];
    private float[] accVal = new float[matrix_size];
    float[] M = new float[matrix_size];
    float[] outR = new float[matrix_size];
    float[] I = new float[matrix_size];
    private MediaPlayer mediaPlayer;
    private AssetFileDescriptor sound1;
    private AssetFileDescriptor sound2;
    private AssetFileDescriptor sound3;
    private AssetFileDescriptor sound4;
    private AssetFileDescriptor sound5;
    private int currentSound = 0;
    private Button minBut;
    private Button spongeBut;
    private boolean isMinion;

    private void setMin()
    {
        sound1 = getApplicationContext().getResources().openRawResourceFd(R.raw.ba);
        sound2 = getApplicationContext().getResources().openRawResourceFd(R.raw.nana);
        sound3 = getApplicationContext().getResources().openRawResourceFd(R.raw.bana);
        sound4 = getApplicationContext().getResources().openRawResourceFd(R.raw.naaa);
        sound5 = getApplicationContext().getResources().openRawResourceFd(R.raw.potato);
        isMinion = true;
    }

    private void setSponge()
    {
        sound1 = getApplicationContext().getResources().openRawResourceFd(R.raw.q1);
        sound2 = getApplicationContext().getResources().openRawResourceFd(R.raw.q2);
        sound3 = getApplicationContext().getResources().openRawResourceFd(R.raw.q3);
        sound4 = getApplicationContext().getResources().openRawResourceFd(R.raw.answer);
        sound5 = getApplicationContext().getResources().openRawResourceFd(R.raw.last);
        isMinion = false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        orientation = new float[3];
        for(int i = 0; i< 3; i++)
        {
            orientation[i] = 0;
        }
//        xView = (TextView) findViewById(R.id.editText);
//        yView = (TextView) findViewById(R.id.editText2);
//        zView = (TextView) findViewById(R.id.editText3);
        minBut = (Button) findViewById(R.id.minBtn);
        spongeBut = (Button) findViewById(R.id.spongeBtn);
        setMin();
        minBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               setMin();
            }
        });
        spongeBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSponge();
            }
        });
        soundStatus = (TextView) findViewById(R.id.editText4);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                {
                    accVal = sensorEvent.values.clone();
                }
                else if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                {
                    magVal = sensorEvent.values.clone();
                }
                boolean success = SensorManager.getRotationMatrix(M, I, accVal, magVal);
                if(success)
                {
                    SensorManager.remapCoordinateSystem(M, SensorManager.AXIS_X, SensorManager.AXIS_Y, outR);
                    SensorManager.getOrientation(outR, orientation);
                    //updateVal();
                    if(orientation[1] > -0.2 && orientation[1] <= 0.2 && orientation[2] > -0.2 && orientation[2] <= 0.2)
                    {
                        playSound(sound1, 1);
                        if(isMinion)
                        {
                            updateSoundStatus("Ba");
                        }
                        else {
                            updateSoundStatus("Who lives in the pineapple under the sea?");
                        }
                    }
                    else if(orientation[1] > -0.2 && orientation[1] <= 0.2 && orientation[2] > -3.2 && orientation[2] <= -2.8)
                    {
                        playSound(sound2, 2);
                        if(isMinion)
                        {
                            updateSoundStatus("NaNa");
                        }
                        else {
                            updateSoundStatus("Absorbant and yellow and porous is he");
                        }

                    }
                    else if(orientation[1] > -0.2 && orientation[1] <= 0.2 && orientation[2] > 1.3 && orientation[2] <= 1.65)
                    {
                        playSound(sound3, 3);
                        if(isMinion)
                        {
                            updateSoundStatus("Bana");
                        }
                        else {
                            updateSoundStatus("If nautical nonsense be something you wish");
                        }

                    }
                    else if(orientation[1] >= -1.7 && orientation[1] < -1.2 && (orientation[2] > -0.1 && orientation[2] <= 0.6 || orientation[2] > 2.8 && orientation[2] <= 3.2))
                    {
                        playSound(sound4, 4);
                        if(isMinion)
                        {
                            updateSoundStatus("Naaaa~~~");
                        }
                        else {
                            updateSoundStatus("SPONGEBOB SQUAREPANTS");
                        }

                    }
                    else if(orientation[2] >= -1.5 && orientation[2] < -1.2 && orientation[1] > -0.2 && orientation[1] <= 0.2)
                    {
                        playSound(sound5, 5);
                        if(isMinion)
                        {
                            updateSoundStatus("Potato");
                        }
                        else {
                            updateSoundStatus("Spongebob squarepants X 3");
                        }

                    }
                    else
                    {
                        //updateSoundStatus("none");
                        currentSound = 0;
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        mediaPlayer = new MediaPlayer();

        //updateVal();

    }

    private void updateSoundStatus(final String sound) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                soundStatus.setText(sound);
            }
        });
    }

    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(listener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listener, magSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onStop()
    {
        sensorManager.unregisterListener(listener);
        super.onStop();
    }

    synchronized void playSound(AssetFileDescriptor afd, int num)
    {
        if(mediaPlayer.isPlaying() || currentSound == num)
        {
            return;
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
            if(num == 1) {
                currentSound = 0;
            }
            else{
                currentSound = num;
            }
        }
        catch (IOException e) {
            Log.d("Media Player Problem: ", "Media Player cannot play");
        }
    }

//    private void updateVal() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                xView.setText("X Value = " + orientation[0]);
//            }
//        });
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                yView.setText("Y Value = " + orientation[1]);
//            }
//        });
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                zView.setText("Z Value = " + orientation[2]);
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


}


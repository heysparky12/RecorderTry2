package com.example.recordertry2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CODE = 123;
    private MediaPlayer play;
    private MediaRecorder record;
    private String FILE; //File path

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set file path
        FILE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tempRecord.m4a";


        //get button1
        Button btn1 = (Button) findViewById(R.id.btn1);
        //click listener
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //action
                Toast.makeText(getApplicationContext(), "Recording...", Toast.LENGTH_SHORT).show();
                try {
                    startRecord();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //get button2
        Button btn2 = (Button) findViewById(R.id.btn2);
        //click listener
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //action
                Toast.makeText(getApplicationContext(), "Stopped Recording", Toast.LENGTH_SHORT).show();
                stopRecord();
            }
        });
        //get btn3
        Button btn3 = (Button) findViewById(R.id.btn3);
        //click listener
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //action
                Toast.makeText(getApplicationContext(), "Playing...", Toast.LENGTH_SHORT).show();
                try {
                    startPlay();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //get btn4
        Button btn4 = (Button) findViewById(R.id.btn4);
        //click listener
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //action
                Toast.makeText(getApplicationContext(), "Stopped Playing", Toast.LENGTH_SHORT).show();
                stopPlay();
            }
        });

        //get permission button
        Button btn5 = (Button) findViewById(R.id.btn5);
        //click listener
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //action
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                PermissionReq();
            }
        });
    }

    public void PermissionReq(){
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                + ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //If not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.RECORD_AUDIO) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Grant Permission");
                builder.setMessage("Record Audio; Read and Write Storage");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{
                                        Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                                },
                                REQUEST_CODE
                        );
                    }
                });
                builder.setNegativeButton("Cancel", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }else{
                //if permissions are already granted
                Toast.makeText(getApplicationContext(), "Permission Already Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void startRecord() throws Exception {

        if (record!=null){
            record.release();
        }
        File fileOut = new File(FILE);
        if (fileOut!=null){
            fileOut.delete(); //overwrite existing file
        }
        record = new MediaRecorder();
        record.setAudioSource(MediaRecorder.AudioSource.MIC);
        record.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        record.setAudioSamplingRate(44100);
        record.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        record.setOutputFile(FILE);
        Log.i("MyApp", "Sup Sucka");
        try {
            record.prepare();
            record.start();
        }
        catch (IOException e) {
            throw new RuntimeException("Exception preparing recorder", e);
        }

    }
    public void stopRecord(){
        //Toast.makeText(getApplicationContext(), "A", Toast.LENGTH_SHORT).show();
        record.stop();
        record.reset();
        record.release();
    }
    public void startPlay() throws Exception {
        if(play!=null){
            play.stop();
            play.release();
        }
        play = new MediaPlayer();
        play.setDataSource(FILE);
        play.prepare();
        play.start();
        //stop when sound ends
        play.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer play) {
                play.release(); //release resources
            }
        });
    }
    public void stopPlay(){
        play.stop();
        play.release();
    }
}
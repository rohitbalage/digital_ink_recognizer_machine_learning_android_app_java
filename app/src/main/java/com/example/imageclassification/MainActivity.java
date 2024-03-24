package com.example.imageclassification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements GetTouch {

    TextView resultTv;
    Button recognize, clear;



    GetTouch getTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recognize = findViewById(R.id.button);
        clear = findViewById(R.id.button2);
        resultTv = findViewById(R.id.textView2);

        DrawingView drawingView = findViewById(R.id.draw);
        getTouch = this;
        drawingView.setGetTouch(getTouch);


        // Specify the recognition model for a language


        //Download the model


        recognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.clear();

            }
        });


    }

    @Override
    public void getTouchMethod(MotionEvent motionEvent) {
        addNewTouchEvent(motionEvent);
    }

    // Call this each time there is a new event.
    public void addNewTouchEvent(MotionEvent event) {

    }
}

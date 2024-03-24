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
import android.widget.Toast;

import com.google.mlkit.common.MlKitException;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.vision.digitalink.DigitalInkRecognition;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier;
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer;
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions;
import com.google.mlkit.vision.digitalink.Ink;


public class MainActivity extends AppCompatActivity implements GetTouch {

    TextView resultTv;
    Button recognize, clear;

    DigitalInkRecognitionModelIdentifier modelIdentifier;

    DigitalInkRecognizer recognizer;

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
        // Specify the recognition model for a language

        try {
            modelIdentifier =
                    DigitalInkRecognitionModelIdentifier.fromLanguageTag("en-US");
        } catch (MlKitException e) {
            // language tag failed to parse, handle error.
        }
        if (modelIdentifier == null) {
            // no model was found, handle error.
        }

        DigitalInkRecognitionModel model =
                DigitalInkRecognitionModel.builder(modelIdentifier).build();

// Get a recognizer for the language


        //Download the model

        RemoteModelManager remoteModelManager = RemoteModelManager.getInstance();

        remoteModelManager
                .download(model, new DownloadConditions.Builder().build())
                .addOnSuccessListener(aVoid -> {
                     recognizer =
                            DigitalInkRecognition.getClient(
                                    DigitalInkRecognizerOptions.builder(model).build());

                })
                .addOnFailureListener(
                        e -> {});


        recognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recognizer != null) {
                    Ink ink = inkBuilder.build();
                    recognizer.recognize(ink)
                            .addOnSuccessListener(
                                    result -> resultTv.setText(result.getCandidates().get(0).getText()))
                            .addOnFailureListener(
                                    e -> {
                                        Log.e("TAG", "Error during recognition: " + e);
                                        Toast.makeText(MainActivity.this, "Recognition failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                } else {
                    // Handle case when recognizer is null
                    Toast.makeText(MainActivity.this, "Recognizer not initialized", Toast.LENGTH_SHORT).show();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.clear();
                inkBuilder = Ink.builder();
                resultTv.setText("");
            }
        });


    }

    @Override
    public void getTouchMethod(MotionEvent motionEvent) {
        addNewTouchEvent(motionEvent);
    }

    // Call this each time there is a new event.

    Ink.Builder inkBuilder = Ink.builder();
    Ink.Stroke.Builder strokeBuilder;

    // Call this each time there is a new event.
    public void addNewTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        long t = System.currentTimeMillis();

        // If your setup does not provide timing information, you can omit the
        // third paramater (t) in the calls to Ink.Point.create
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                strokeBuilder = Ink.Stroke.builder();
                strokeBuilder.addPoint(Ink.Point.create(x, y, t));
                break;
            case MotionEvent.ACTION_MOVE:
                strokeBuilder.addPoint(Ink.Point.create(x, y, t));
                break;
            case MotionEvent.ACTION_UP:
                strokeBuilder.addPoint(Ink.Point.create(x, y, t));
                inkBuilder.addStroke(strokeBuilder.build());
                strokeBuilder = null;
                break;
        }
    }


}

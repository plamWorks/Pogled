package com.uni.sofia.fmi.pogled;

import static com.uni.sofia.fmi.pogled.Constants.REQUEST_CAMERA_PERMISSION;
import static com.uni.sofia.fmi.pogled.Constants.REQUEST_IMAGE_CAPTURE;
import static com.uni.sofia.fmi.pogled.Constants.SCORE_THRESHOLD;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.task.vision.detector.Detection;

import java.util.List;

public class MainLoopActivity extends AppCompatActivity {

    private ImageView imageView;

    private Camera camera;

    private ObjectDetectionModel objectDetectionModel;

    private SpeechProducer speechProducer;

    private RecognizedObjectsDrawer recognized_object_drawer;

    private FileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tts);

        imageView = findViewById(R.id.image_view);

        recognized_object_drawer = new RecognizedObjectsDrawer();

        fileManager = new FileManager(getFilesDir());

        // start object detection model
        objectDetectionModel = new ObjectDetectionModel(this);
        // start TTS engine
        speechProducer = new SpeechProducer(getApplicationContext(), getAssets());
        //start camera feed
        camera = new Camera(this, fileManager);

        camera.checkCameraPermission();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = camera.retrievePicture();
            Bitmap mutableBitmap = imageBitmap.copy(imageBitmap.getConfig(), true);
            Canvas canvas = new Canvas(mutableBitmap);
            //run the model
            List<Detection> results = objectDetectionModel.detect(imageBitmap);

            for (Detection detection : results) {
                String label = detection.getCategories().get(0).getLabel();
                float score = detection.getCategories().get(0).getScore();
                if (score > SCORE_THRESHOLD) {
                    //draw bounding box
                    recognized_object_drawer.drawDetection(canvas, detection, imageBitmap.getWidth(), imageBitmap.getHeight());
                    //speak out loud the name of the object in bulgarian
                    speechProducer.sayObjectLabel(label);
                }
                //display updated contents of imageview
                imageView.setImageBitmap(mutableBitmap);
            }

            imageView.setOnClickListener(view -> {

                camera.launchCamera();
                view.setOnClickListener(null);
            });

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            camera.checkIfPermissionGranted(grantResults);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fileManager.deleteTempFile();
        objectDetectionModel.shutDown();
        speechProducer.shutDown();
    }
}
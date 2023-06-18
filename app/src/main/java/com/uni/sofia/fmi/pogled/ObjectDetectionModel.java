package com.uni.sofia.fmi.pogled;

import static com.uni.sofia.fmi.pogled.Constants.MODEL_INPUT_HEIGHT;
import static com.uni.sofia.fmi.pogled.Constants.MODEL_INPUT_WIDTH;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.task.vision.detector.Detection;
import org.tensorflow.lite.task.vision.detector.ObjectDetector;

import java.nio.MappedByteBuffer;
import java.util.List;

public class ObjectDetectionModel {


    private ObjectDetector objectDetector;

    public ObjectDetectionModel(Context context) {
        try {
            MappedByteBuffer modelBuffer = FileUtil.loadMappedFile(context, "lite-model_efficientdet_lite4_detection_metadata_2.tflite");
            ObjectDetector.ObjectDetectorOptions.Builder optionsBuilder = ObjectDetector.ObjectDetectorOptions.builder();
            objectDetector = ObjectDetector.createFromBufferAndOptions(modelBuffer, optionsBuilder.build());
        } catch (final Exception e) {
            Toast.makeText(context, "Failed to load model", Toast.LENGTH_SHORT).show();
            System.exit(1);
        }
    }

    public List<Detection> detect(Bitmap image) {
        Bitmap scaledImage = Bitmap.createScaledBitmap(image, MODEL_INPUT_WIDTH, MODEL_INPUT_HEIGHT, true);
        return objectDetector.detect(TensorImage.fromBitmap(scaledImage));
    }

    public void shutDown() {
        objectDetector.close();
    }
}

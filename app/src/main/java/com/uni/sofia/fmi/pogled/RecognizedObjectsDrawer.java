package com.uni.sofia.fmi.pogled;

import static com.uni.sofia.fmi.pogled.Constants.*;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

import org.tensorflow.lite.task.vision.detector.Detection;

public class RecognizedObjectsDrawer {

    private final Paint paint = new Paint();

    private final Matrix matrix;

    public RecognizedObjectsDrawer() {
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15.0f);
        matrix = new Matrix();

    }

    public synchronized void drawDetection(final Canvas canvas, Detection detection, int imageWidth, int imageHeight) {
        matrix.setScale(imageWidth / (float)MODEL_INPUT_WIDTH,
                imageHeight / (float)MODEL_INPUT_HEIGHT);
        RectF detectedBox = detection.getBoundingBox();
        matrix.mapRect(detectedBox);
        canvas.drawRect(detectedBox, this.paint);
    }
}

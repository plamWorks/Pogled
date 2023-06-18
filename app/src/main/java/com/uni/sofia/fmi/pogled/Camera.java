package com.uni.sofia.fmi.pogled;

import static com.uni.sofia.fmi.pogled.Constants.REQUEST_CAMERA_PERMISSION;
import static com.uni.sofia.fmi.pogled.Constants.REQUEST_IMAGE_CAPTURE;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.content.FileProvider;

public class Camera {

    private Activity activity;
    private FileManager fileManager;

    public Camera(Activity activity, FileManager fileManager) {
        this.activity = activity;
        this.fileManager = fileManager;
    }
    public  void checkCameraPermission() {
        if (activity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            this.launchCamera();
        } else {
            String[] permission = {Manifest.permission.CAMERA};
            activity.requestPermissions(permission, REQUEST_CAMERA_PERMISSION);
        }
    }
    public void launchCamera() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileManager.deleteTempFile();
            Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName(), fileManager.getTempFile());
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (Exception e) {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public Bitmap retrievePicture() {
        Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName(), fileManager.getTempFile());
        activity.getContentResolver().notifyChange(uri, null);
        ContentResolver cr = activity.getContentResolver();
        Bitmap imageBitmap = null;
        try {
            imageBitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, uri);
        }
        catch (Exception e) {
            Toast.makeText(activity, "Failed to load", Toast.LENGTH_SHORT).show();
        }
        return imageBitmap;
    }
    public void checkIfPermissionGranted(int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            Toast.makeText(activity, "Camera permission denied.", Toast.LENGTH_SHORT).show();
        }
    }
}

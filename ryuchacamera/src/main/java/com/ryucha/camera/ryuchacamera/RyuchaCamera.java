package com.ryucha.camera.ryuchacamera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HSI on 2019-04-09.
 */

public class RyuchaCamera {
    final static String TAG = "RYUCHA_CAMERA";
    public static Camera.PictureCallback pictureCallback = null;
    public static float angle = 0;

    public static boolean checkCameraHardware(Context context){
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else{
            return false;
        }
    }

    public static Camera getCameraInstance()
    {
        Camera c = null;
        try{
            c = Camera.open();
        }catch(Exception e){
            Log.d(TAG, e.toString());
        }

        return c;
    }

    public static Camera getCameraInstance(int num)
    {
        Camera c = null;
        try{
            c = Camera.open(num);
        }catch(Exception e){
            Log.d(TAG, e.toString());
        }

        return c;
    }

    public static void createPictureCallback(final String path){
        pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                Log.d(TAG, "CapturePath : " + path);
                try {
                    Bitmap picture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Bitmap resultBitmap = RyuchaCamera.rotateImage(picture, angle);
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

//                    ExifInterface exif = new ExifInterface(path);
//                    Log.d(TAG, exif.getAttribute(ExifInterface.TAG_ORIENTATION));

                    FileOutputStream fos = new FileOutputStream(path + "/" + format.format(date) + ".jpg");
                    resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                camera.startPreview();

            }
        };
    }

    public static Bitmap rotateImage(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0,0, source.getWidth(), source.getHeight(), matrix, true);
    }


}

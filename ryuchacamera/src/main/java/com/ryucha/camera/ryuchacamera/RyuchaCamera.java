package com.ryucha.camera.ryuchacamera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

import java.io.File;

/**
 * Created by HSI on 2019-04-09.
 */

public class RyuchaCamera {
    final static String TAG = "RYUCHA_CAMERA";

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

}

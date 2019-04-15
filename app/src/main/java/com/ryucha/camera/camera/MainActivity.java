package com.ryucha.camera.camera;

import android.Manifest;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ryucha.camera.ryuchacamera.RyuchaCamera;
import com.ryucha.camera.ryuchacamera.RyuchaCameraPreview;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MAIN_ACTIVITY";
    private Camera mCamera;
    private RyuchaCameraPreview mPreview;
    private Display display;
    private Button captureButton;


    private OrientationEventListener orientationEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        setPermission();

        mCamera = RyuchaCamera.getCameraInstance();
        if(mCamera != null) {
            mPreview = new RyuchaCameraPreview(this, mCamera);

            final FrameLayout preview = findViewById(R.id.camera_preview);
            preview.addView(mPreview);

            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TestPicture";
            File dir = new File(path);

            if (!dir.mkdirs()) {
                Log.d(TAG, "Directory Not Create");
            } else {
                Log.d(TAG, "Directory Create!!");
            }

            orientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
                @Override
                public void onOrientationChanged(int i) {
                    if(i >=315 || i < 45){
                        RyuchaCamera.angle = 90;
                    } else if(i >= 45 && i < 135){
                        RyuchaCamera.angle = 180;
                    } else if(i >= 135 && i < 225){
                        RyuchaCamera.angle = 270;
                    } else if(i >= 225 && i < 315){
                        RyuchaCamera.angle = 0;
                    }
                }
            };

            orientationEventListener.enable();
            RyuchaCamera.createPictureCallback(path);
            captureButton = findViewById(R.id.button_capture);
            captureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mCamera.takePicture(null, null, RyuchaCamera.pictureCallback);
                    } catch (Exception e) {
                        Log.d(TAG, e.toString());
                    }
                }
            });
        }
    }

    private void setPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

    }

}

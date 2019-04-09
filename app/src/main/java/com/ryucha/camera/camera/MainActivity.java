package com.ryucha.camera.camera;

import android.Manifest;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    private Button captureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setPermission();

        mCamera = RyuchaCamera.getCameraInstance();

        mPreview = new RyuchaCameraPreview(this, mCamera);
        FrameLayout preview = findViewById(R.id.camera_preview);
        preview.addView(mPreview);


        captureButton = findViewById(R.id.button_capture);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.takePicture(null, null, pictureCallback);
            }
        });

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
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            Log.d(TAG, "캠쳐");

            camera.startPreview();
        }
    } ;
}

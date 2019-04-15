package com.ryucha.camera.ryuchacamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HSI on 2019-04-09.
 */

public class RyuchaCameraPreview extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder mHolder;
    private Camera mCamera;
    static final String TAG = "RYUCHA_CAMERA_PREVIEW";

    private List<Camera.Size> pictureSizeList ;
    private List<Camera.Size> previewSizeList;
    private Camera.Parameters parameters;

    private Camera.PreviewCallback previewCallback = null;

    private RyuchaCameraPreview preview;
    private ImageView callbackPreview;

    public RyuchaCameraPreview(Context context, Camera camera) {
        super(context);
        preview = this;
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mCamera.setDisplayOrientation(90);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        parameters = mCamera.getParameters();
        pictureSizeList = parameters.getSupportedPictureSizes();
        for(Camera.Size size : pictureSizeList){
            Log.d(TAG, "PICTURE SIZE  = Width : " + size.width + "  Height:  " + size.height);
        }

        previewSizeList = parameters.getSupportedPreviewSizes();
        for(Camera.Size size : previewSizeList){
            Log.d(TAG, "PREVIEW SIZE = Width : " +size.width + "  Height : " + size.height ) ;
        }


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        mCamera.startPreview();
        try{
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        }
        catch(IOException e){
            Log.d(TAG, "ERROR setting camera preview: " + e.getMessage() );
        }

        parameters.setPreviewFormat(ImageFormat.NV21);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        float ratio = (float)width / height;
        int count = 0;
        Log.d(TAG, width + " : "  + height + "비율 : " + ratio);
        for(Camera.Size size : pictureSizeList){
            if(ratio == (float)size.height / size.width){
                count++;
            }

            if(count == 4)
            {
                Log.d(TAG, "picture check" + size.width + " : " + size.height);
                parameters.setPictureSize(size.width, size.height);
                break;
            }
        }
        if(mHolder.getSurface() == null){
            return ;
        }
        mCamera.setParameters(parameters);
        try{
            mCamera.stopPreview();
        } catch(Exception e){
            Log.d(TAG, "ERROR stop Preview" + e.getMessage());
        }

        try{
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error starting camera preview : " + e.getMessage());
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     *
     * @param mode
     * mode = 1 => Flash OFF
     * mode = 2 => Flash ON
     * mode = 3 => Flash Auto
     * mode = 4 => Flash TORCH
     */
    public void setFlesh(int mode){
        mCamera.stopPreview();
        switch (mode){
            case 1:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                break;
            case 2:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                break;
            case 3:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                break;
            case 4:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                break;

        }
        mCamera.setParameters(parameters);

        mCamera.startPreview();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);

        return false;
    }

    private void touchFocus(Rect r){
        List<Camera.Area> focus = new ArrayList<Camera.Area>();

        focus.add(new Camera.Area(r, 1000));

        parameters.setMeteringAreas(focus);
        parameters.setFocusAreas(focus);

        mCamera.setParameters(parameters);
    }



 }

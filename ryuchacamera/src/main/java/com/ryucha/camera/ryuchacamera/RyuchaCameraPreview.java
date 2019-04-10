package com.ryucha.camera.ryuchacamera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
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
    public RyuchaCameraPreview(Context context, Camera camera) {
        super(context);
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
        try{
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch(IOException e){
            Log.d(TAG, "ERROR setting camera preview: " + e.getMessage() );
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        float ratio = (float)width / height;
        int count = 0;
        Log.d(TAG, width + " : "  + height + "비율 : " + ratio);

        for(Camera.Size size : previewSizeList){
            if(ratio == (float)size.height/  size.width){
                count++;
            }

            if(count == 1)
            {
                Log.d(TAG, "preview check" + size.width + " : " + size.height);
                parameters.setPreviewSize(size.width, size.height);
                break;
            }
        }

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
}

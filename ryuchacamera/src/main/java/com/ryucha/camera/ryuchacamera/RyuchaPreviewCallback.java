package com.ryucha.camera.ryuchacamera;

import android.hardware.Camera;
import android.view.SurfaceHolder;

public interface RyuchaPreviewCallback {
    void onCallbakcData(Camera camera, byte[] bytes, SurfaceHolder sh);
}

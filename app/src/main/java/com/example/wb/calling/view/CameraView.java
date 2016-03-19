package com.example.wb.calling.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by wb on 16/3/13.
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private boolean preview = false;

    public CameraView(Context context ,Camera camera) {
        super(context);
        mCamera = camera;
        initCameraParmaters();
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    private void initCameraParmaters() {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewFrameRate(5); //每秒5帧
        parameters.setPictureFormat(PixelFormat.JPEG);//设置照片的输出格式
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.set("rotation", 90);
        parameters.set("orientation", "portrait");
        parameters.set("jpeg-quality", 85);//照片质量
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("sv","create");
        if(mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
                preview = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("sv","changed");
        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
            preview = false;
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }
        // make any resize, rotate or reformatting changes here
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            preview = true;
        } catch (Exception e){
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
//        Log.d("sv","destroyed");
//        if (mCamera != null) {
//            if (preview) {
//                mCamera.stopPreview();
//                preview = false;
//            }
//            mCamera.release();
//            mCamera = null; // 记得释放
//        }
    }
}

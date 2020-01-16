//package com.smartit.jobSeeker.activities;
//
//import android.app.Activity;
//import android.content.Context;
//import android.hardware.Camera;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//
//import java.io.IOException;
//
//public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
//
//    private SurfaceHolder mHolder;
//    private Camera mCamera;
//    private Activity mActivity;
//
//    public MyCameraSurfaceView(Context context, Camera camera, Activity activity) {
//        super(context);
//        mCamera = camera;
//        mActivity = activity;
//        // Install a SurfaceHolder.Callback so we get notified when the
//        // underlying surface is created and destroyed.
//        mHolder = getHolder();
//        mHolder.addCallback(this);
//        // deprecated setting, but required on Android versions prior to 3.0
//        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//    }
//
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        try {
//            setCameraDisplayOrientation(mActivity, 0, mCamera);
//            previewCamera();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void previewCamera() {
//        try {
//            mCamera.setPreviewDisplay(mHolder);
//            mCamera.startPreview();
//        } catch (Exception e) {
//            //Log.d(APP_CLASS, "Cannot start preview", e);
//        }
//    }
//
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        // TODO Auto-generated method stub
//        // The Surface has been created, now tell the camera where to draw the preview.
//        try {
//            mCamera.setPreviewDisplay(holder);
//            mCamera.startPreview();
//        } catch (IOException e) {
//        }
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        // TODO Auto-generated method stub
//
//    }
//
//
//
//
//    public static void setCameraDisplayOrientation(Activity activity,
//                                                   int cameraId, android.hardware.Camera camera) {
//
//        android.hardware.Camera.CameraInfo info =
//                new android.hardware.Camera.CameraInfo();
//
//        android.hardware.Camera.getCameraInfo(cameraId, info);
//
//        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
//        int degrees = 0;
//
//        switch (rotation) {
//            case Surface.ROTATION_0:
//                degrees = 0;
//                break;
//            case Surface.ROTATION_90:
//                degrees = 90;
//                break;
//            case Surface.ROTATION_180:
//                degrees = 180;
//                break;
//            case Surface.ROTATION_270:
//                degrees = 270;
//                break;
//        }
//
//        int result;
//        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            result = (info.orientation + degrees) % 360;
//            result = (360 - result) % 360;  // compensate the mirror
//        } else {  // back-facing
//            result = (info.orientation - degrees + 360) % 360;
//        }
//        VideoWithSurfaceVw.orientation = result;
//        camera.setDisplayOrientation(result);
//    }
//}

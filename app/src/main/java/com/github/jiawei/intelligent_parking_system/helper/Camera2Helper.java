package com.github.jiawei.intelligent_parking_system.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Camera2Helper {

    private HandlerThread handlerThread;
    private Handler childHandler;

    private NetWorkHelper netWorkHelper;

    private CameraManager cameraManager;
    private String cameraId;
    private CameraCharacteristics mCameraCharacteristics;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;

    private TextureView textureView;
    private ImageReader imageReader;

    private Activity activity;



    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    ///为了使照片竖直显示
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


    public Camera2Helper(Activity activity, TextureView textureView) {
        this.activity = activity;
        this.textureView = textureView;
        init();
    }

    private void init() {
        netWorkHelper=new NetWorkHelper();


        handlerThread = new HandlerThread("Camera2Thread");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());

        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                initCarema();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                releaseCamera();
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }


    private void initCarema() {
        cameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraIds = cameraManager.getCameraIdList();
            if (cameraIds.length <= 0) {
                Toast.makeText(activity, "该设备不支持拍照！", Toast.LENGTH_LONG).show();
                return;
            }

            for (String id : cameraIds) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(id);
                if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                    cameraId = id;
                    mCameraCharacteristics = characteristics;
                }
            }


            imageReader = ImageReader.newInstance(720, 1080, ImageFormat.JPEG, 1);
            imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = reader.acquireNextImage();
                    ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
                    final byte[] bytes = new byte[byteBuffer.remaining()];

                    byteBuffer.get(bytes);

                    final long time=System.currentTimeMillis();

                    String dirPath= Environment.getExternalStorageDirectory().getPath()+"/tcp/image";
                    final File dir=new File(dirPath);
                    if(!dir.exists()){
                        dir.mkdirs();
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            File file=new File(dir,""+time+".jpg");
                            try {
                                FileOutputStream outputStream = new FileOutputStream(file);
                                outputStream.write(bytes);

                                outputStream.close();
                                Toast.makeText(activity,"文件已保存，文件名为："+time
                                        +".jpg",Toast.LENGTH_LONG).show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                    NetWorkHelper.send(bytes);








                    image.close();
                }
            }, childHandler);

            openCamera();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    private void openCamera() throws CameraAccessException {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
            @Override
            public void onOpened(CameraDevice camera) {
                cameraDevice=camera;
                try {
                    createPreviewSession(camera);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDisconnected(CameraDevice camera) {

            }

            @Override
            public void onError(CameraDevice camera, int error) {

            }
        }, childHandler);
    }


    private void createPreviewSession(final CameraDevice cameraDevice) throws CameraAccessException {
        final CaptureRequest.Builder captureRequestBuilder=cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        Surface surface=new Surface(textureView.getSurfaceTexture());
        captureRequestBuilder.addTarget(surface);
        captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);     // 闪光灯
        captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE); // 自动对焦
        cameraDevice.createCaptureSession(Arrays.asList(surface, imageReader.getSurface()), new CameraCaptureSession.StateCallback() // ③
        {
            @Override
            public void onConfigured(CameraCaptureSession session) {
                if (null == cameraDevice) return;
                // 当摄像头已经准备好时，开始显示预览
                cameraCaptureSession = session;
                try {
                    session.setRepeatingRequest(captureRequestBuilder.build(), null, childHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                Toast.makeText(activity, "配置失败", Toast.LENGTH_SHORT).show();
            }
        },childHandler);

    }

    private void releaseCamera(){
            cameraCaptureSession.close();
            cameraCaptureSession = null;

            cameraDevice.close();
            cameraDevice = null;

            imageReader.close();
            imageReader = null;

    }


    /**
     * 拍照
     */
    public void takePicture() {
        if (cameraDevice == null) {
            return;
        }
        try {
            CaptureRequest.Builder  captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            // 将imageReader的surface作为CaptureRequest.Builder的目标
            captureRequestBuilder.addTarget(imageReader.getSurface());
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);  // 自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);  //自动闪光

            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();              // 获取手机方向
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));              // 根据设备方向计算设置照片的方向

            cameraCaptureSession.capture(captureRequestBuilder.build(),null, childHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}




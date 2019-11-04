package com.github.jiawei.intelligent_parking_system.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.jiawei.intelligent_parking_system.R;
import com.github.jiawei.intelligent_parking_system.event.CaptureEvent;
import com.github.jiawei.intelligent_parking_system.helper.Camera2Helper;
import com.github.jiawei.intelligent_parking_system.helper.NetWorkHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Camera2Activity extends AppCompatActivity {

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    ///为了使照片竖直显示
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private TextureView textureView;
    //private SurfaceHolder mSurfaceHolder;
    private ImageView iv_show;
    private ImageButton btnTakePic;
    private CameraManager mCameraManager;//摄像头管理器
    private Handler childHandler, mainHandler;
    private String mCameraID;//摄像头Id 0 为后  1 为前
    private ImageReader mImageReader;
    private CameraCaptureSession mCameraCaptureSession;
    private CameraDevice mCameraDevice;

    private CaptureRequest captureRequest;

    private Camera2Helper helper=null;

    private NetWorkHelper netWorkHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        textureView = (TextureView) findViewById(R.id.textureView);

        initView();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , 2);
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}
                    , 1);
        }else {
            helper = new Camera2Helper(this,textureView);
        }

    }


//    public Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what==0){
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        helper.takePicture();
//                    }
//                });
//            }
//        }
//    };


    /**
     * 初始化
     */
    private void initView() {

        netWorkHelper=new NetWorkHelper();

        new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        netWorkHelper.startServer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
         }).start();




        //   mSurfaceView.setOnClickListener(this);
        btnTakePic = findViewById(R.id.btnTakePic);
        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.takePicture();
            }
        });





//        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
//            @Override
//            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
//                // 初始化Camera
//                initCamera2();
//            }
//
//            @Override
//            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//
//            }
//
//            @Override
//            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//                // 释放Camera资源
//                if (null != mCameraDevice) {
//                    mCameraDevice.close();
//                    Camera2Activity.this.mCameraDevice = null;
//                }
//                return true;
//            }
//
//            @Override
//            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//
//            }
//        });

        //   mSurfaceHolder = mSurfaceView.getHolder();
        //  mSurfaceHolder.setKeepScreenOn(true);
        // mSurfaceView添加回调
//            mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
//                @Override
//                public void surfaceCreated(SurfaceHolder holder) { //SurfaceView创建
//                    // 初始化Camera
//                    initCamera2();
//                }
//
//                @Override
//                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//                }
//
//                @Override
//                public void surfaceDestroyed(SurfaceHolder holder) { //SurfaceView销毁
//
//                }
//            });
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void capture(CaptureEvent event){
        System.out.println("Capture Start in activity...");
        helper.takePicture();
    }

    //    /**
//     * 初始化Camera2
//     */
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void initCamera2() {
//        HandlerThread handlerThread = new HandlerThread("Camera2");
//        handlerThread.start();
//        childHandler = new Handler(handlerThread.getLooper());
//        mainHandler = new Handler(getMainLooper());
//
//
//        mCameraID = "" + CameraCharacteristics.LENS_FACING_FRONT;//后摄像头
//        mImageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1);
//        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
//            @Override
//            public void onImageAvailable(ImageReader reader) {
//                //处理图像数据
//                //可以在这里处理拍照得到的临时照片 例如，写入本地
//                // mCameraDevice.close();
//                //textureView.setVisibility(View.GONE);
//                //iv_show.setVisibility(View.VISIBLE);
//                // 拿到拍照照片数据
//                Image image = reader.acquireNextImage();
//                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
//                byte[] bytes = new byte[buffer.remaining()];
//                buffer.get(bytes);
//
//                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//
//
////                String fileName=Environment.getExternalStorageDirectory()+"/hjw/"+System.
////                        currentTimeMillis()+".png";
////                File file=new File(fileName);
////                file.getParentFile().mkdirs();
////                try {
////                    file.createNewFile();
////                    FileOutputStream fileOutputStream=new FileOutputStream(file);
////                    bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
////                    fileOutputStream.flush();
////                    fileOutputStream.close();
////                    Toast.makeText(Camera2Activity.this,"照片已保存,",Toast.LENGTH_LONG).show();
////
////
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//
//                image.close();
//
//                //由缓冲区存入字节数组
////                if (bitmap != null) {
////                    iv_show.setImageBitmap(bitmap);
////                }
//            }
//        }, mainHandler);
//
//        //获取摄像头管理
//        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//        try {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            //打开摄像头
//            mCameraManager.openCamera(mCameraID, stateCallback, mainHandler);
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 摄像头创建监听
//     */
//    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
//        @Override
//        public void onOpened(CameraDevice camera) {//打开摄像头
//            mCameraDevice = camera;
//            //开启预览
//            takePreview();
//        }
//
//        @Override
//        public void onDisconnected(CameraDevice camera) {//关闭摄像头
//            Log.i("CAMERA","onDisconnected");
////            if (null != mCameraDevice) {
////                mCameraDevice.close();
////                Camera2Activity.this.mCameraDevice = null;
////            }
//        }
//
//        @Override
//        public void onError(CameraDevice camera, int error) {//发生错误
//            Toast.makeText(Camera2Activity.this, "摄像头开启失败", Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    /**
//     * 开始预览
//     */
//    private void takePreview() {
//        try {
//            // 创建预览需要的CaptureRequest.Builder
//            final CaptureRequest.Builder previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//            // 将SurfaceView的surface作为CaptureRequest.Builder的目标
//            Surface surface = new Surface(textureView.getSurfaceTexture());
//            previewRequestBuilder.addTarget(surface);
//            // 自动对焦
//            previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
//            // 打开闪光灯
//            previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
//
//            // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
//            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()), new CameraCaptureSession.StateCallback() // ③
//            {
//                @Override
//                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
//                    if (null == mCameraDevice) return;
//                    // 当摄像头已经准备好时，开始显示预览
//                    mCameraCaptureSession = cameraCaptureSession;
//                    try {
//                        mCameraCaptureSession.setRepeatingRequest(previewRequestBuilder.build(), null, childHandler);
//                    } catch (CameraAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
//                    Toast.makeText(Camera2Activity.this, "配置失败", Toast.LENGTH_SHORT).show();
//                }
//            }, childHandler);
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 点击事件
//     */
//    @Override
//    public void onClick(View v) {
//        takePicture();
//    }
//
//    /**
//     * 拍照
//     */
//    private void takePicture() {
//        if (mCameraDevice == null) {
//            return;
//        }
//
//        // 创建拍照需要的CaptureRequest.Builder
//        final CaptureRequest.Builder captureRequestBuilder;
//
//        try {
//            captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
//            // 将imageReader的surface作为CaptureRequest.Builder的目标
//            captureRequestBuilder.addTarget(mImageReader.getSurface());
//            // 自动对焦
//            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
//            // 自动曝光
//            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
//
//
//            // 获取手机方向
//            int rotation = getWindowManager().getDefaultDisplay().getRotation();
//            // 根据设备方向计算设置照片的方向
//            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
//            //拍照
////
////            if (captureRequest == null) {
////                captureRequest = captureRequestBuilder.build();
////            }
//
//            mCameraCaptureSession.capture(captureRequestBuilder.build(),null, childHandler);
//
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length <= 0) {
                Toast.makeText(this, "请授予照相机权限！", Toast.LENGTH_LONG).show();
                return;
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("打开摄像机。。");
                //打开摄像头
                //initView();
                helper = new Camera2Helper(this,textureView);
            } else {
                Toast.makeText(this, "请授予照相机权限！", Toast.LENGTH_LONG).show();
            }
        }

    }
}

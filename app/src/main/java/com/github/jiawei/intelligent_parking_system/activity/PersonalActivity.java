package com.github.jiawei.intelligent_parking_system.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jiawei.intelligent_parking_system.R;
import com.github.jiawei.intelligent_parking_system.bean.UserBean;
import com.github.jiawei.intelligent_parking_system.utils.CacheManager;
import com.github.jiawei.intelligent_parking_system.utils.MyDbManager;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PersonalActivity extends AppCompatActivity {

    private ImageView headPority;
    private  int  CHOOSE_PHOTO=2;
    public static  final int  TAKE_PHOTO=1;
    private Uri imageUri;

    private DbManager dbManager;

    private TextView billTv;
    private TextView holdTv;
    private TextView nameTv;
    private TextView introduceTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        dbManager=MyDbManager.getDbManger();
        billTv=findViewById(R.id.bill_tv);
        holdTv=findViewById(R.id.hold_tv);
        introduceTv=findViewById(R.id.introduce_tv);
        nameTv=findViewById(R.id.name_tv);





        headPority=findViewById(R.id.head_portrait_img);
        headPority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    if (ContextCompat.checkSelfPermission(PersonalActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE
                )!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(PersonalActivity.this,new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },1);
                }else {

                }*/

            File outImg=new File(getExternalCacheDir(),"output_image.png");
                try {
                    if (outImg.exists()){
                        outImg.delete();
                    }
                    outImg.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT>24){
                    imageUri=FileProvider.getUriForFile(PersonalActivity.this,
                            "com.github.jiawei.intelligent_parking_system.activity.fileprovider",outImg);
                }else{
                    imageUri=Uri.fromFile(outImg);
                }
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,outImg);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });

        getData();
    }


    private void getData(){
        Intent intent=getIntent();
        String name=intent.getStringExtra("number");
        if (name!=null){

            UserBean bean= null;
            try {
                bean = dbManager.selector(UserBean.class).where("account","=",name)
                        .findFirst();
            } catch (DbException e) {
                e.printStackTrace();
            }
            if (bean!=null){
                nameTv.setText(bean.getAccount());
                headPority.setImageDrawable(CacheManager.changePicture(bean.getImageUrl()));
                introduceTv.setText(bean.getIntroduce());
                holdTv.setText(""+bean.getHoldCount());
                billTv.setText(""+bean.getBill());
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode==RESULT_OK){
                    try {
                        Bitmap bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        headPority.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

        }
    }

    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }
        }
    }
}

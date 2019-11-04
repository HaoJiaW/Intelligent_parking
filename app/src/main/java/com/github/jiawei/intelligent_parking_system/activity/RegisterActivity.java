package com.github.jiawei.intelligent_parking_system.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.jiawei.intelligent_parking_system.MainActivity;
import com.github.jiawei.intelligent_parking_system.R;
import com.github.jiawei.intelligent_parking_system.bean.UserBean;
import com.github.jiawei.intelligent_parking_system.event.RefreshUserDataEvent;
import com.github.jiawei.intelligent_parking_system.helper.MyDataBaseHelper;
import com.github.jiawei.intelligent_parking_system.utils.CacheManager;
import com.github.jiawei.intelligent_parking_system.utils.MyDbManager;
import com.kongzue.dialog.v2.TipDialog;

import org.greenrobot.eventbus.EventBus;
import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity  implements View.OnClickListener {

    private EditText userRegNumber;
    private EditText userRegPassword;
    private Button registerBtn;
    private ImageView backImg;
    private DbManager dbManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbManager=MyDbManager.getDbManger();


        initView();
    }

    private void initView(){
        userRegNumber=findViewById(R.id.register_number);
        userRegPassword=findViewById(R.id.register_password);
        registerBtn=findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(this);
        backImg=findViewById(R.id.reg_back_img);
        backImg.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_btn:
                if (!validText()){
                    return;
                }
                String numberText=userRegNumber.getText().toString();
                String passwordText=userRegPassword.getText().toString();

                UserBean bean=new UserBean();
                bean.setAccount(numberText);
                bean.setPassword(passwordText);
                bean.setUserName("用户"+numberText);
                bean.setIntroduce("");
                int bill=(int)(Math.random()*10000+1);
                bean.setBill(bill);
                int count=(int)(Math.random()*20+1);
                bean.setHoldCount(count);
                bean.setImageUrl(CacheManager.getPicture(getResources().getDrawable(R.drawable.dlam)));
                try {
                    dbManager.saveOrUpdate(bean);
                } catch (DbException e) {
                    e.printStackTrace();
                }



            /*    SQLiteDatabase db=LoginActivity.helper.getWritableDatabase();
                String numberText=userRegNumber.getText().toString();
                String passwordText=userRegPassword.getText().toString();

                Cursor cursor=db.query("User",null,null,null,
                        null,null,null);
                if (cursor.moveToFirst()){
                    do {
                        String number = cursor.getString(cursor.getColumnIndex("number"));
                        String password = cursor.getString(cursor.getColumnIndex("password"));
                        Log.i("数据库信息","数据库手机号:"+ number+"数据库密码:"+ password);
                        if (numberText.equals(number)){
                            Toast.makeText(this,"该手机号已被注册!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }while(cursor.moveToNext());
                }


                ContentValues values=new ContentValues();
                values.clear();
                values.put("number",numberText);
                values.put("password",passwordText);
                //插入数据
                db.insert("User",null,values);*/

                Toast.makeText(this,"注册成功!",Toast.LENGTH_SHORT).show();
                EventBus.getDefault().postSticky(new RefreshUserDataEvent(numberText));
                finish();
                break;
            case R.id.reg_back_img:
                finish();
                break;
        }



    }

    private Boolean validText(){
        String numberText=userRegNumber.getText().toString();
        String passwordText=userRegPassword.getText().toString();
        if (numberText.isEmpty()||passwordText.isEmpty()){
            TipDialog.show(this, "账号或密码不能为空", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
            return false;
        }




        if (numberText.length()!=11){
            TipDialog.show(this, "请输入十一位数号码", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
            return false;
        }

        if (passwordText.length()<6){
            TipDialog.show(this, "密码不得小于6位", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
            return false;
        }


        return true;
    }



}

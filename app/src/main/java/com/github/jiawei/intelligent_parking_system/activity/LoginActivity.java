package com.github.jiawei.intelligent_parking_system.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jiawei.intelligent_parking_system.MainActivity;
import com.github.jiawei.intelligent_parking_system.R;
import com.github.jiawei.intelligent_parking_system.helper.MyDataBaseHelper;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

    private EditText userNumber;
    private EditText userPassword;
    private Button loginBtn;
    private TextView regUserTv;
    private Boolean isLoginSuccess;
    public static MyDataBaseHelper helper;

    @ViewInject(R.id.login_back_img)
    private ImageView backImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        x.view().inject(this);
        //user.db是数据库名,User是表名
        helper=new MyDataBaseHelper(this,"user.db",null,1);
        //创建数据库
        helper.getWritableDatabase();

        initView();

    }

    private void initView(){
        userNumber=findViewById(R.id.login_number);
        userPassword=findViewById(R.id.login_password);
        loginBtn=findViewById(R.id.login_btn);
        regUserTv=findViewById(R.id.register_new_tv);
        loginBtn.setOnClickListener(this);
        regUserTv.setOnClickListener(this);

    }

    @Event(value = R.id.login_back_img,type = View.OnClickListener.class)
    private void finishAcitivy(View v){
        finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_btn:
                isLoginSuccess=false;
                SQLiteDatabase database=helper.getWritableDatabase();
               /* ContentValues values=new ContentValues();
                values.put("number",userNumber.getText().toString());
                values.put("password",userNumber.getText().toString());
                //1.表名2.给某些可为空的列赋NULL3.数据
                database.insert("User",null,values);*/


                //查询表里的数据
                Cursor cursor=database.query("User",null,null,null,
                        null,null,null);

                if (cursor.moveToFirst()){
                    do {
                        String number = cursor.getString(cursor.getColumnIndex("number"));
                        String password = cursor.getString(cursor.getColumnIndex("password"));
                        String numberText = userNumber.getText().toString();
                        String passwordText = userPassword.getText().toString();;
                        if (number.equals(numberText)&&password.equals(passwordText)){
                            isLoginSuccess=true;
                        }

                    }while(cursor.moveToNext());
                }

                if (isLoginSuccess){
                    //Toast.makeText(this,"登录成功!",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(this,MainActivity.class);
                    intent.putExtra("number",userNumber.getText().toString());
                    startActivity(intent);


                }else {
                    Toast.makeText(this,"登录失败!",Toast.LENGTH_SHORT).show();
                }

                cursor.close();
                break;

            case R.id.register_new_tv:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
                break;
        }
    }
}

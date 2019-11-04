package com.github.jiawei.intelligent_parking_system.activity;

import android.app.Application;

import com.kongzue.dialog.v2.DialogSettings;

import org.xutils.x;

//import cn.bmob.v3.Bmob;

import static com.kongzue.dialog.v2.DialogSettings.THEME_DARK;
import static com.kongzue.dialog.v2.DialogSettings.THEME_LIGHT;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库
        x.Ext.init(this);

        DialogSettings.style = DialogSettings.STYLE_KONGZUE;
        DialogSettings.tip_theme =DialogSettings. THEME_LIGHT;         //设置提示框主题为亮色主题
        DialogSettings.dialog_theme = DialogSettings.THEME_LIGHT;


      //  Bmob.initialize(this,"6f60840083dc9fac6b803f4d4d9a916e","bmob");

    }
}

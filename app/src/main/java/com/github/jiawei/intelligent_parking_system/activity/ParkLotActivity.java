package com.github.jiawei.intelligent_parking_system.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.github.jiawei.intelligent_parking_system.R;
import com.github.jiawei.intelligent_parking_system.dialog.AppointmentDialog;

public class ParkLotActivity extends AppCompatActivity implements View.OnClickListener {

    private Button appointBtn;
    private ImageView backImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_lot);
        appointBtn=findViewById(R.id.appointment_btn);
        appointBtn.setOnClickListener(this);
        backImg=findViewById(R.id.parklot_back_img);
        backImg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.appointment_btn:
                //设置自定义Dialog的圆角风格
                AppointmentDialog appointmentDialog = new AppointmentDialog(this);
                Window window=appointmentDialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                appointmentDialog.setCanceledOnTouchOutside(true);//点击屏幕不消失
                appointmentDialog.show();
                //设置参数必须在show之后，不然没有效果
                WindowManager.LayoutParams params = appointmentDialog.getWindow().getAttributes();
                appointmentDialog.getWindow().setAttributes(params);
                //  new BdParamDialog(getActivity()).show();
                break;
            case R.id.parklot_back_img:
                finish();
                break;
        }
    }
}

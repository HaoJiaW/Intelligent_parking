package com.github.jiawei.intelligent_parking_system.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.github.jiawei.intelligent_parking_system.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppointmentDialog extends Dialog {

    private View mView;

    private TextView timeSelect;
    private   Calendar selectedDate = Calendar.getInstance();

    private Calendar startDate = Calendar.getInstance();

    private Calendar endDate = Calendar.getInstance();

    public AppointmentDialog(Context context) {
        super(context, R.style.Theme_dialog);
        initView();
        timeSelect=mView.findViewById(R.id.select_time_tv);
        endDate.set(2019,5,31,12,30);



        final TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                timeSelect.setText(getTime(date));
            }
        })  .setType(new boolean[]{false, true, true, true, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择时间")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(getContext().getResources().getColor(R.color.white))//标题文字颜色
                .setSubmitColor(getContext().getResources().getColor(R.color.white))//确定按钮文字颜色
                .setCancelColor(getContext().getResources().getColor(R.color.white))//取消按钮文字颜色
                .setTitleBgColor(getContext().getResources().getColor(R.color.black))//标题背景颜色 Night mode
                .setBgColor(getContext().getResources().getColor(R.color.cx_snr_text_color))//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true)//是否显示为对话框样式
                .build();

        timeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });
    }

    private String getTime(Date date){
        String newDate="";
        SimpleDateFormat format=new SimpleDateFormat("MM-dd HH:mm");
        newDate=format.format(date);
        return newDate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
    }


    private void initView(){
        LayoutInflater inflater=LayoutInflater.from(getContext());
        mView=inflater.inflate(R.layout.dialog_appointment,null);
    }
}

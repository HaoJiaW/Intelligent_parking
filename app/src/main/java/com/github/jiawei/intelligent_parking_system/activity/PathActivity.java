package com.github.jiawei.intelligent_parking_system.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.jiawei.intelligent_parking_system.R;

public class PathActivity extends AppCompatActivity {



    private Canvas canvas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

//        canvas=new Canvas();
//
//        Paint trianglePaint = new Paint();
//        trianglePaint.setColor(Color.BLUE);
//        trianglePaint.setStyle(Paint.Style.STROKE);  //设置为描边的画笔
//        trianglePaint.setStrokeWidth(5f);  //画笔的宽度
//
//
//        Path path=new Path();
//        path.moveTo(100,100);
//        path.lineTo(100,400);
//        path.lineTo(400,400);
//        path.close();
//
//        canvas.drawPath(path,trianglePaint);

    }
}

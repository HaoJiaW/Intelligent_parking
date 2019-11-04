package com.github.jiawei.intelligent_parking_system.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyView extends View {

    private Canvas canvas;

    public MyView(Context context) {
        this(context,null);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        canvas=new Canvas();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas=canvas;
        super.onDraw(canvas);
    }


    public void setProgress(int progress){
        Paint paint=new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.TRANSPARENT);

        canvas.save();
    /*    canvas.drawRect(0,getMeasuredHeight()*(progress/100)
                ,getMeasuredWidth(),getMeasuredHeight(),paint);*/
        //canvas.clipRect(0,0,)
        canvas.restore();
        invalidate();
        Log.i("MyView","canvas已重绘");
    }

    public void setProgress2(int progress){
        Paint paint=new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.TRANSPARENT);

        canvas.save();
        Path path=new Path();
        path.addRect(0,0,getMeasuredWidth(),getMeasuredHeight()*(1-progress/100), Path.Direction.CW);
        canvas.clipPath(path);
        canvas.restore();
        invalidate();
        Log.i("MyView","canvas已裁剪重绘");
    }

}

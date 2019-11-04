package com.github.jiawei.intelligent_parking_system.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.jiawei.intelligent_parking_system.R;
import com.github.jiawei.intelligent_parking_system.utils.ScreenUnitUtils;

public class CustomImageView extends View {

    private CustomImageView imageView;

    public CustomImageView(Context context) {
        this(context,null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomImageView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setLayoutParams(new RelativeLayout.LayoutParams(ScreenUnitUtils.Px2Dp(getContext(),128)
                ,ScreenUnitUtils.Px2Dp(getContext(),128)));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        Paint paint=new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.TRANSPARENT);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),paint);
        canvas.save();
        canvas.clipRect(0,0,getMeasuredWidth(),getMeasuredHeight()/2);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.full_reduce);
        canvas.drawBitmap(bitmap,0,0,null);
        canvas.restore();
    }
}

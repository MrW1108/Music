package com.example.y.study.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class PeopleView extends View {
    public PeopleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w=getWidth();
        float h=getHeight();

        Paint paint=new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        canvas.drawCircle(w/2,h/4,h/8,paint);
        canvas.save();
        canvas.translate(w/2,3*h/8);
        canvas.drawLine(0,0,0,h/4,paint);
        canvas.drawLine(-w/4,h/16,w/4,h/16,paint);
        canvas.restore();       //此方法需在save()之后调用，因为它是将save()之后的绘图与save()之前合并

        // 如果没有save()操作，translate()和rotate()方法将基于上一次移动进行移动或旋转
        canvas.translate(w/2,5*h/8);
        canvas.drawLine(0,0,-h/8,h/4,paint);
        canvas.drawLine(0,0,h/8,h/4,paint);
    }
}

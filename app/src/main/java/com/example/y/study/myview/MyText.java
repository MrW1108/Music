package com.example.y.study.myview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class MyText extends TextView {
    float wid;
    Paint mPaint;
    LinearGradient linearGradient;
    Matrix matrix;
    float translate;
    public MyText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (matrix!=null){
            translate+=wid/5;
            if (translate>2*wid){
                translate=-wid;
            }
            matrix.setTranslate(translate,0);
            linearGradient.setLocalMatrix(matrix);
            postInvalidateDelayed(100);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (wid==0){
            wid=getMeasuredWidth();
            if (wid>0){
                mPaint=getPaint();
                linearGradient=new LinearGradient(0,0,wid,0,new int[]{Color.BLUE,0xffffffff,0xffed0f0f,Color.BLUE},null, Shader.TileMode.CLAMP);
                mPaint.setShader(linearGradient);
                matrix=new Matrix();
            }
        }
    }


}

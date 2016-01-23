package com.example.wb.calling.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.andreabaccega.widget.FormEditText;

/**
 * Created by wb on 16/1/23.
 */
public class MEditText extends FormEditText{

    public MEditText(Context context) {
        super(context);
    }

    public MEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint mpaint = new Paint();
        mpaint.setStyle(Paint.Style.FILL);
        mpaint.setStrokeWidth(10);
        mpaint.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        canvas.drawRect(
                0,getHeight()+10,getWidth(),getHeight()+20,mpaint
        );
        canvas.save();
        canvas.restore();
    }
}

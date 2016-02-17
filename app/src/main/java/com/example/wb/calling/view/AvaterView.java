package com.example.wb.calling.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.wb.calling.R;

/**
 * Created by wb on 16/2/4.
 */
public class AvaterView extends ImageView{
    public AvaterView(Context context) {
        super(context);
    }

    public AvaterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AvaterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        Drawable drawable = getDrawable();
        Bitmap bitmap;
        if(drawable != null){
            BitmapDrawable db= (BitmapDrawable) getDrawable();
            bitmap = db.getBitmap();
        }else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_person);
        }
        BitmapShader shader = new BitmapShader(
                bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP
        );
        Paint mPaint = new Paint();
        mPaint.setShader(shader);

        int w = getWidth();
        int h = getHeight();
        float scale = getResources().getDisplayMetrics().density;
        int r = (int) (40 * scale + 0.5f);

        canvas.drawCircle(w/2,h/2,r,mPaint);
    }
}

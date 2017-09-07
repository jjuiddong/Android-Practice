package com.example.bitmaprender;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import javax.annotation.Resource;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        RenderView view = new RenderView(this);
        setContentView(view);
    }

    public class RenderView extends View {
        Bitmap mBmp;
        Paint mPaint;

        public RenderView(Context context) {
            super(context);

            Resources res = getResources();
            BitmapDrawable bd = (BitmapDrawable)res.getDrawable(R.drawable.sora);
            mBmp = bd.getBitmap();
            mPaint = new Paint();

            Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();
        }

        public void onDraw(Canvas canvas) {
            //canvas.drawBitmap(mBmp, 10, 10, mPaint);
            Rect src = new Rect();
            src.set(0,0,100,100);
            Rect dst  = new Rect();
            dst.set(0,0,canvas.getWidth(),canvas.getHeight());
            canvas.drawBitmap(mBmp, null, dst, mPaint);

        }
    }

}

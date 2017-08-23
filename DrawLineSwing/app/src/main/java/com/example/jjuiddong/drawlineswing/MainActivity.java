package com.example.jjuiddong.drawlineswing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        MyView m = new MyView(MainActivity.this);
        ll.addView(m);
        setContentView(ll);
    }

    class MyView extends View {
        Paint paint = new Paint();
        Path path = new Path();
        public MyView(Context context) {
            super(context);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10f);
            path.moveTo(350,0);
            path.lineTo(350,600);

            final Handler handler = new Handler();
            class MyRunnable implements Runnable {
                private Handler handler;
                private MyView myView;
                double mAngle = 0;
                boolean mRight = true;
                public MyRunnable(Handler handler, MyView view) {
                    this.handler = handler;
                    this.myView = view;
                }
                @Override
                public void run() {
                    this.handler.postDelayed(this, 30);

                    if (mRight)
                    {
                        mAngle += 0.02f;
                        if (mAngle > 1.f)
                            mRight = false;
                    }
                    else
                    {
                        mAngle -= 0.02f;
                        if (mAngle < -1.f)
                            mRight = true;
                    }

                    double y = 600 * Math.cos(mAngle);
                    double x = 350 + 600 * Math.sin(mAngle);

                    path.reset();
                    path.moveTo(350,0);
                    path.lineTo((int)x, (int)y);
                    myView.invalidate();
                }
            }
            handler.post(new MyRunnable(handler, this));
        }

        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }
    }

}

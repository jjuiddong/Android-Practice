package com.example.canvastest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        RenderView view = new RenderView(this);
        setContentView(view);

    }

     class RenderView extends View {

         public RenderView(Context context) {
             super(context);
         }

        public void onDraw(Canvas canvas) {

            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            canvas.drawRect(10, 10, 100, 100, paint);


        }
    }

}

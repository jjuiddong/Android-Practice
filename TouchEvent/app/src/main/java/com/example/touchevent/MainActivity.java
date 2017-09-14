package com.example.touchevent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View vw = new View(this);
        setContentView(vw);
        //View vw = (View)findViewById(R.layout.activity_main);

        vw.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        int a = 0;
        return true;
    }
}

package com.example.activitytest;

        import android.content.Context;
        import android.content.Intent;
        import android.icu.lang.UCharacter;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.MotionEvent;
        import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //MainView view = new MainView(this);
        //setContentView(view);
    }

    public void Update() {
        int a  = 0;
    }

    public boolean onTouchEvent(MotionEvent event) {

        Intent intent = new Intent(MainActivity.this, SubActivity.class);
        MainActivity.this.startActivity(intent);

        return true;
    }

//    public class MainView extends View {
//        public MainView(Context context) {
//            super(context);
//        }
//
//        public boolean onTouchEvent(MotionEvent event) {
//            super.onTouchEvent(event);
//
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                Intent intent = new Intent(MainActivity.this, SubActivity.class);
//                MainActivity.this.startActivity(intent);
//            }
//            return true;
//        }
//    }

}

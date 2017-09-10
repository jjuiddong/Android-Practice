package com.example.alertdialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        MainView view = new MainView(this);
        setContentView(view);
    }

    public class MainView extends View {
        public MainView(Context context) {
            super(context);
        }

        public boolean onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                AlertDialog.Builder bld = new AlertDialog.Builder(MainActivity.this);
                bld.setTitle("Caption");
                bld.setMessage("Comment");
                bld.setPositiveButton("Close", null);
//                bld.setPositiveButton("Close", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
                bld.show();
            }

            return true;
        }

    }
}

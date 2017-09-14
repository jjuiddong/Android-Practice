package com.example.fileiotest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements View.OnTouchListener {

    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> arData = new ArrayList<String>();
        mAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arData);
        ListView list = (ListView)findViewById(R.id.logList);
        mAdapter.add("ssss");
        list.setAdapter(mAdapter);

        try {
            FileInputStream fis = openFileInput("MousePos.txt");
            FileChannel inChannel = fis.getChannel();

            final int s = fis.available();
            ByteBuffer buff = ByteBuffer.allocate((int)fis.available());
            buff.clear();

            inChannel.read(buff);

            buff.rewind();

            final int posSize = s/4;
            float[] pos = new float[posSize];
            buff.asFloatBuffer().get(pos);

            inChannel.close();

            for (int i=0; i < posSize; i+=2)
            {
                mAdapter.add("" + pos[i] + ", " + pos[i+1]);
            }

            fis.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        list.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_UP)
        {
            mAdapter.add("" + ev.getAxisValue(0) + ", " + ev.getAxisValue(1));

            try {
                FileOutputStream fos = openFileOutput ("MousePos.txt", Context.MODE_APPEND);
                FileChannel outChannel = fos.getChannel();

                float[] pos = new float[2];
                pos[0] = ev.getAxisValue(0);
                pos[1] = ev.getAxisValue(1);

                ByteBuffer buff = ByteBuffer.allocate(4*2);
                buff.clear();
                buff.asFloatBuffer().put(pos);

                outChannel.write(buff);
                outChannel.close();
                fos.close();

                FileInputStream fis = openFileInput ("MousePos.txt");
                int a = fis.available();
                int b = 0;

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        return false;
    }

}

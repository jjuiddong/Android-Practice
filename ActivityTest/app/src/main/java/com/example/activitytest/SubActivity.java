package com.example.activitytest;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by 재정 on 2017-09-10.
 */

public class SubActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subactivity_main);
    }

}

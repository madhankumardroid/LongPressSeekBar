package com.madhan.longpresssseekbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView tvValue;
    private static final int STOP_PROGRESS = 1;
    private static final int DURATION_TO_DETECT_TOUCH_EVENT = 2000;
    final Handler longPressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOP_PROGRESS:
                    Toast.makeText(MainActivity.this, "Long click event detected for: " + tvValue.getText().toString(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "handleMessage: long click event detected");
                    break;
            }
        }
    };

    private Runnable handlerRunnable = new Runnable() {
        @Override
        public void run() {
            Message msg = longPressHandler.obtainMessage(STOP_PROGRESS);
            longPressHandler.sendMessage(msg);
            longPressHandler.removeCallbacks(handlerRunnable);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SeekBar sbCustom = (SeekBar) findViewById(R.id.sb_custom);
        tvValue = (TextView) findViewById(R.id.textView);
        sbCustom.setMax(15);//You can customize this value for your need
        tvValue.setText(String.valueOf(sbCustom.getProgress()));
        sbCustom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvValue.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        sbCustom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "onTouch: Action down event");
                        setupTimeoutHandler(DURATION_TO_DETECT_TOUCH_EVENT);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "onTouch: Action up event");
                        longPressHandler.removeCallbacks(handlerRunnable);
                        break;
                }
                return false;
            }
        });
    }

    private void setupTimeoutHandler(long timeout) {
        longPressHandler.postDelayed(handlerRunnable, timeout);
    }
}
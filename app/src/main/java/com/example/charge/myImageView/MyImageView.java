package com.example.charge.myImageView;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MyImageView extends androidx.appcompat.widget.AppCompatImageView {
    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int SERVER_ERROR = 3;
    private Handler handler = new Handler() {
        @Override
        public void publish(LogRecord logRecord) {

        }

        @Override
        public void flush() {

        }

        @Override
        public void close() throws SecurityException {

        }
    };
    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}

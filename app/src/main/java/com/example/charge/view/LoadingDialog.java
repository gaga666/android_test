package com.example.charge.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.charge.R;

public class LoadingDialog extends Dialog {

    /**
     * 加载框提示文字
     */
    private TextView mTextView;

    public LoadingDialog(@NonNull Context context) {
        //使用自定义的Style
        super(context, R.style.WinDialog);
        //使用自定义的layout
        setContentView(R.layout.dialog_loading);
        mTextView = (TextView) findViewById(R.id.loading_message);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setText(String s) {
        if (mTextView != null) {
            mTextView.setText(s);
            mTextView.setVisibility(View.VISIBLE);
        }
    }

    public void setText(int res) {
        if (mTextView != null) {
            mTextView.setText(res);
            mTextView.setVisibility(View.VISIBLE);
        }
    }
}

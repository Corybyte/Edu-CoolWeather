package com.coolweather.android.module;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.coolweather.android.R;

public class XProgressBar extends Dialog {

    private TextView textView;

    @SuppressLint("MissingInflatedId")
    public XProgressBar(@NonNull Context context) {
        super(context);
        // 去掉对话框标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.xprogressbar);

        // 绑定视图
        textView = findViewById(R.id.text);
    }

    // 设置文本
    public void setText(String text){
        textView.setText(text);
    }
}

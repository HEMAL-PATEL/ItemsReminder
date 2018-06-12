package com.example.oluwagbenga.reminder.views;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;

public class MyEditText extends AppCompatEditText {

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditText(Context context) {
        super(context);
    }

    public String getValue(){
        return getText().toString();

    }

    public boolean isNull(){
        String val = getText().toString();
        return TextUtils.isEmpty(val);
    }
}

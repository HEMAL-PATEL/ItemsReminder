package com.example.oluwagbenga.reminder.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class NumberedTextView extends AppCompatTextView {

    public NumberedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setItemNumbers();
    }

    private void setItemNumbers(){
        String text = 1 +" "+getText().toString();
       // String[] items = text.split(",");
        setText(text);
    }
}

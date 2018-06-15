package com.example.aquashdw.emptyroomfinder;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by aquashdw on 18. 5. 24.
 */

public class TimeImageView extends AppCompatImageView {

    public TimeImageView(Context context){
        super(context);
    }

    public TimeImageView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public TimeImageView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }
}

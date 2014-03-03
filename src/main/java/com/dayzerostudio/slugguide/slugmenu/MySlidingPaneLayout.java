package com.dayzerostudio.slugguide.slugmenu;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MySlidingPaneLayout extends SlidingPaneLayout {
    private static final String TAG = MySlidingPaneLayout.class.getSimpleName();

    //private static final String TAG = MySlidingPaneLayout.class.getSimpleName();

    public MySlidingPaneLayout(Context context) {
        super(context);
    }

    public MySlidingPaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //Log.d(TAG, ((Float)ev.getX()).toString());
        if (this.isOpen())
            return super.onInterceptTouchEvent(ev);
        return ev.getX() <= 110 && super.onInterceptTouchEvent(ev);
    }

}


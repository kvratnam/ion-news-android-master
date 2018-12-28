package com.mantra.ionnews.utils;

import android.content.Context;
import android.support.design.widget.VisibilityAwareImageButton;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.mantra.ionnews.R;


public class HgSquareFloatButton extends android.support.v7.widget.AppCompatImageButton {
    private String customAttr;

    public HgSquareFloatButton(Context context ) {
        this( context, null );
    }

    public HgSquareFloatButton(Context context, AttributeSet attrs ) {
        this( context, attrs, R.attr.imageButtonStyle );
    }

    public HgSquareFloatButton(Context context, AttributeSet attrs,
                               int defStyle ) {
        super( context, attrs, defStyle );

    }
}

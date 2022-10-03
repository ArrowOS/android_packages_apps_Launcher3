package com.android.launcher3.qsb;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.launcher3.R;

public final class AssistantIconView extends ImageView {

    public AssistantIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AssistantIconView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void setIcon() {
        setScaleType(ScaleType.CENTER);
        setImageResource(R.drawable.ic_mic_color);
    }
}
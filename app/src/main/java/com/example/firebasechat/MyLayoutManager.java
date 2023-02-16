package com.example.firebasechat;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

public class MyLayoutManager extends LinearLayoutManager {

public MyLayoutManager(Context context) {
        super(context);
        }

public MyLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        }

public MyLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        }

@Override
public boolean supportsPredictiveItemAnimations() {
        return false;
        }
        }
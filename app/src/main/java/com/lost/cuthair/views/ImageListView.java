package com.lost.cuthair.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 图片自定义listView
 * Created by lost on 2016/5/23.
 */
public class ImageListView extends ListView {


    public ImageListView(Context context) {
        super(context);
    }

    public ImageListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

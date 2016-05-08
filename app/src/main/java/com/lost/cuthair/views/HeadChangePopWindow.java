package com.lost.cuthair.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.lost.cuthair.R;

/**
 * Created by lost on 2016/5/8.
 */
public class HeadChangePopWindow extends PopupWindow{

    private Button btn_take_photo;// 拍照
    private Button btn_select_photo;// 选择相册
    private View v;

    public HeadChangePopWindow(Activity context, View.OnClickListener itemOnclick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.pop_head, null);
        btn_select_photo = (Button) v.findViewById(R.id.btn_select_photo);
        btn_take_photo = (Button) v.findViewById(R.id.btn_take_photo);
        btn_take_photo.setOnClickListener(itemOnclick);
        btn_select_photo.setOnClickListener(itemOnclick);
        this.setContentView(v);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.MyDialogStyleFromBottom);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = v.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;

            }
        });
    }
}

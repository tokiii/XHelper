package com.lost.cuthair.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lost.cuthair.R;
import com.lost.cuthair.activities.AddBusinessActivity;
import com.lost.cuthair.activities.BusinessRecordActivity;
import com.lost.cuthair.dao.Business;
import com.lost.cuthair.utils.DateUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lost on 2016/4/17.
 */
public class BusinessAdapter extends BaseAdapter {

    private List<Business> lists;
    private Context context;
    private HashMap<Integer, View> map;
    private ImageLoaderConfiguration configuration;
    private ImageLoader imageLoader;


    public BusinessAdapter(List<Business> lists, Context context) {
        this.lists = lists;
        this.context = context;
        map = new HashMap<>();
        configuration = ImageLoaderConfiguration
                .createDefault(context);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(configuration);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Business business = lists.get(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_businessrecord, null);
        LinearLayout ll_left = (LinearLayout) convertView.findViewById(R.id.ll_left);
        LinearLayout ll_right = (LinearLayout) convertView.findViewById(R.id.ll_right);
        TextView tv_left = (TextView) convertView.findViewById(R.id.tv_left);
        TextView tv_right = (TextView) convertView.findViewById(R.id.tv_right);
        ImageView iv_left = (ImageView) convertView.findViewById(R.id.iv_left);
        ImageView iv_right = (ImageView) convertView.findViewById(R.id.iv_right);
        RelativeLayout rl_group = (RelativeLayout) convertView.findViewById(R.id.rl_group);


//        tv_time.setText(business.getDate().toString());
        Uri imageUri = Uri.parse("file://" + business.getImage());
        if (lists.size()!=0 && position % 2 == 0) {
            ll_right.setVisibility(View.GONE);
            ll_left.setVisibility(View.VISIBLE);
            tv_left.setText(business.getBusinessInfo());
            if (business.getImage() != null) {
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.mipmap.ic_launcher)
                        .showImageOnFail(R.mipmap.ic_launcher)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();
                String imageUrl = ImageDownloader.Scheme.FILE.wrap(business.getImage());
                imageLoader.displayImage(imageUrl, iv_left, options);
            }
            ll_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent left_intent = new Intent(context, AddBusinessActivity.class);
                    left_intent.putExtra("businessId", business.getId());
                    context.startActivity(left_intent);
                }
            });

            // 测量控件的宽高
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            ll_left.measure(w, h);
            int height = ll_left.getMeasuredHeight();
            Log.i("info", "得到的高度为------>" + height + "    位置---》" + position);


            // 动态添加view
            ImageView imageView = new ImageView(context);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(5, height);
            layoutParams.setMargins(BusinessRecordActivity.screenWidth / 2, 0, 0, 0);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundColor(context.getResources().getColor(R.color.colorBackGround));
            rl_group.addView(imageView);

            Log.i("info", position + ":" + "height------>" + height);
            RelativeLayout.LayoutParams textLayoutParams = new RelativeLayout.LayoutParams(150, 100);
            textLayoutParams.setMargins(BusinessRecordActivity.screenWidth / 2 - 75, height / 2 - 50, 0, 0);
            TextView tv_time = new TextView(context);

            Log.i("info", "left------->" + tv_time.getText().toString() + position);
            tv_time.setText((DateUtil.getMonth(business.getDate()) + 1) + "." + DateUtil.getDay(business.getDate()));
            tv_time.setLayoutParams(textLayoutParams);
            tv_time.setGravity(Gravity.CENTER);
            tv_time.setTextColor(Color.WHITE);
            tv_time.setTextSize(15);
            rl_group.addView(tv_time);

        } else {
            ll_left.setVisibility(View.GONE);
            ll_right.setVisibility(View.VISIBLE);
            tv_right.setText(business.getBusinessInfo());
            if (business.getImage() != null) {
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.mipmap.ic_launcher)
                        .showImageOnFail(R.mipmap.ic_launcher)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();
                String imageUrl = ImageDownloader.Scheme.FILE.wrap(business.getImage());
                imageLoader.displayImage(imageUrl, iv_right, options);
            }
            ll_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent left_intent = new Intent(context, AddBusinessActivity.class);
                    left_intent.putExtra("businessId", business.getId());
                    context.startActivity(left_intent);
                }
            });


            // 测量控件的宽高
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            ll_right.measure(w, h);
            int height = ll_right.getMeasuredHeight();
            // 动态添加view
            ImageView imageView = new ImageView(context);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(5, height);
            layoutParams.setMargins(BusinessRecordActivity.screenWidth / 2, 0, 0, 0);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundColor(context.getResources().getColor(R.color.colorBackGround));
            rl_group.addView(imageView);


            RelativeLayout.LayoutParams textLayoutParams = new RelativeLayout.LayoutParams(150, 100);
            textLayoutParams.setMargins(BusinessRecordActivity.screenWidth / 2 - 75, height / 2 - 50, 0, 0);
            Log.i("info", position + ":" + "height------>" + height);

            TextView tv_time = new TextView(context);
            tv_time.setText((DateUtil.getMonth(business.getDate()) + 1) + "." + DateUtil.getDay(business.getDate()));
            tv_time.setLayoutParams(textLayoutParams);
            tv_time.setGravity(Gravity.CENTER);
            tv_time.setTextColor(Color.WHITE);
            tv_time.setTextSize(15);
            rl_group.addView(tv_time);
        }

        return convertView;
    }
}

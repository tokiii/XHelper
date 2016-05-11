package com.lost.cuthair.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lost.cuthair.R;
import com.lost.cuthair.utils.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * 图片适配器
 * Created by lost on 2016/5/11.
 */
public class ImageAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private ImageLoaderConfiguration configuration;
    private ImageLoader imageLoader;
    private isDelete isDelete;


    public ImageAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        configuration = ImageLoaderConfiguration
                .createDefault(context);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(configuration);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.item_image, null);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_records);

        TextView tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                isDelete.deleted(list, position);
                notifyDataSetChanged();
            }
        });

        ImageUtils.useImageLoaderSetImage(imageLoader, imageView, list.get(position));
        return convertView;
    }

    public void setIsDelete(isDelete isDelete) {
        this.isDelete = isDelete;
    }

    public interface isDelete {
        void deleted(List<String> list, int position);
    }
}

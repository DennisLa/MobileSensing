package de.dennis.mobilesensing.models;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import de.dennis.mobilesensing.R;

public class ImageAdapter extends BaseAdapter {
    private Context context;

    public Integer[] images = {
            R.drawable.pic_1, R.drawable.pic_2,
            R.drawable.pic_3, R.drawable.pic_4,
            R.drawable.pic_5, R.drawable.pic_6,
            R.drawable.pic_7, R.drawable.pic_8,
            R.drawable.pic_9, R.drawable.pic_10,
            R.drawable.pic_11, R.drawable.pic_21,
            R.drawable.pic_12, R.drawable.pic_22,
            R.drawable.pic_13, R.drawable.pic_23,
            R.drawable.pic_14, R.drawable.pic_24,
            R.drawable.pic_15, R.drawable.pic_16,
            R.drawable.pic_17, R.drawable.pic_18,
            R.drawable.pic_19, R.drawable.pic_20,
            R.drawable.pic_25, R.drawable.pic_26,
            R.drawable.pic_default
    };

    public ImageAdapter (Context c) {
        this.context = c;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(images[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(240,240));
        return imageView;
    }
}

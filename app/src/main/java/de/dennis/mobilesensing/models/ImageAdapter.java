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
            R.drawable.a_pic_1, R.drawable.a_pic_2,
            R.drawable.a_pic_3, R.drawable.a_pic_4,
            R.drawable.a_pic_5, R.drawable.a_pic_6,
            R.drawable.a_pic_7, R.drawable.a_pic_8,
            R.drawable.a_pic_9, R.drawable.a_pic_10,
            R.drawable.a_pic_11, R.drawable.a_pic_21,
            R.drawable.a_pic_12, R.drawable.a_pic_22,
            R.drawable.a_pic_13, R.drawable.a_pic_23,
            R.drawable.a_pic_14, R.drawable.a_pic_24,
            R.drawable.a_pic_15, R.drawable.a_pic_16,
            R.drawable.a_pic_17, R.drawable.a_pic_18,
            R.drawable.a_pic_19, R.drawable.a_pic_20,
            R.drawable.a_pic_25, R.drawable.a_pic_26,
            R.drawable.a_pic_27, R.drawable.a_pic_28,
            R.drawable.a_pic_29, R.drawable.a_pic_30,
            R.drawable.a_pic_31, R.drawable.a_pic_default
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

package de.dennis.mobilesensing.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.dennis.mobilesensing.R;

public class ListViewAdapter extends ArrayAdapter<String> {

    Context context;
    String rTitle[];
    String rDescription[];
    int rImgs[];
    private ArrayList<Location> locationsList = new ArrayList<>();


    public ListViewAdapter (Context c, String title[], String description[], int imgs[]) {
        super(c, R.layout.row_listview, R.id.main_title, title);
        this.context = c;
        this.rTitle = title;
        this.rDescription = description;
        this.rImgs = imgs;

    }

    public ListViewAdapter(Context c, ArrayList<Location> locationsList, String title[]) {
        super(c, R.layout.row_listview, R.id.main_title, title);
        this.context = c;
        this.locationsList = locationsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row_listview, parent, false);
        ImageView images = row.findViewById(R.id.item_image);
        TextView myTitle = row.findViewById(R.id.main_title);
        TextView myDescription = row.findViewById(R.id.sub_title);

        // now set our resources on views
//        images.setImageResource(rImgs[position]);
//        myTitle.setText(rTitle[position]);
//        myDescription.setText(rDescription[position]);

        images.setImageResource(locationsList.get(position).getImage());
        myTitle.setText(locationsList.get(position).getTitle());
        myDescription.setText(locationsList.get(position).getTitle());
        return row;
    }
}



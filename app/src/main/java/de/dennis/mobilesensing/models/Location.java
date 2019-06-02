package de.dennis.mobilesensing.models;

import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

public class Location {
    private LatLng position;
    //private ImageView imageView;

    public Location(LatLng position) {
        this.position = position;
        //this.imageView = imageView;
    }

    public Location() {
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

//    public ImageView getImageView() {
//        return imageView;
//    }
//
//    public void setImageView(ImageView imageView) {
//        this.imageView = imageView;
//    }
}

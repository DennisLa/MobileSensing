package de.dennis.mobilesensing.models;

import com.google.android.gms.maps.model.LatLng;

public class Location {
    private LatLng position;
    private String title;
    private  String description;
    //private ImageView imageView;

    public Location(LatLng position, String title, String description) {
        this.position = position;
        this.description = description;
        this.title = title;
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

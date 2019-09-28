package de.dennis.mobilesensing.models;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Location implements Parcelable {
    private LatLng position;
    private String title;
    private String description;
    private int image;
    private Drawable picture; // from phone upload image
//private ImageView imageView;


    public Location(LatLng position, String title, String description, Drawable picture) {
        this.position = position;
        this.title = title;
        this.description = description;
        this.picture = picture;
    }

    public Location(LatLng position, String title, String description) {
        this.position = position;
        this.description = description;
        this.title = title;

        //this.imageView = imageView;
    }

    public Location(LatLng position, String title, String description, int image) {
        this.position = position;
        this.description = description;
        this.title = title;
        this.image = image;

        //this.imageView = imageView;
    }

    public Location() {
    }

    protected Location(Parcel in) {
        position = in.readParcelable(LatLng.class.getClassLoader());
        title = in.readString();
        description = in.readString();
    }

    public Drawable getPicture() {
        return picture;
    }

    public void setPicture(Drawable picture) {
        this.picture = picture;
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(position);
        dest.writeString(title);
        dest.writeString(description);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    //    public ImageView getImageView() {
//        return imageView;
//    }
//
//    public void setImageView(ImageView imageView) {
//        this.imageView = imageView;
//    }

}

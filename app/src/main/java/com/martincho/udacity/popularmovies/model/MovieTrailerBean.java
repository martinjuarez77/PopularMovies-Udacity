package com.martincho.udacity.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by martincho on 28/02/17.
 */

public class MovieTrailerBean implements Parcelable {

    private String name;
    private String size;
    private String source;
    private String type;

    public MovieTrailerBean(){

    }

    public MovieTrailerBean(Parcel _trailerBean) {
        this.name = _trailerBean.readString();
        this.size = _trailerBean.readString();
        this.source = _trailerBean.readString();
        this.type = _trailerBean.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(size);
        parcel.writeString(source);
        parcel.writeString(type);
    }


    public static final Parcelable.Creator<MovieTrailerBean> CREATOR = new Parcelable.Creator<MovieTrailerBean>() {
        public MovieTrailerBean createFromParcel(Parcel movieTrailerBean) {
            return new MovieTrailerBean(movieTrailerBean);
        }

        public MovieTrailerBean[] newArray(int size) {
            return new MovieTrailerBean[size];
        }
    };
}

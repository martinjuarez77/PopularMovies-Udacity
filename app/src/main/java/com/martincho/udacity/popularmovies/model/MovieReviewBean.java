package com.martincho.udacity.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by martincho on 28/02/17.
 */

public class MovieReviewBean  implements Parcelable {

    private String author;

    private String content;

    public MovieReviewBean(){

    }

    public MovieReviewBean(Parcel _reviewBean) {
        this.author = _reviewBean.readString();
        this.content = _reviewBean.readString();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);
    }


    public static final Parcelable.Creator<MovieReviewBean> CREATOR = new Parcelable.Creator<MovieReviewBean>() {
        public MovieReviewBean createFromParcel(Parcel movieReviewBean) {
            return new MovieReviewBean(movieReviewBean);
        }

        public MovieReviewBean[] newArray(int size) {
            return new MovieReviewBean[size];
        }
    };


}

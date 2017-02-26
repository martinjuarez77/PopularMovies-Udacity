package com.martincho.udacity.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by martincho on 28/1/17.
 */

public class MovieBean  implements Parcelable {

    private String title;
    private String releaseDate;
    private String moviePoster;
    private String voteAverage;
    private String plotSynopsis;

    public MovieBean(){

    }

    public MovieBean(Parcel _movieBean) {
        this.title = _movieBean.readString();
        this.releaseDate = _movieBean.readString();
        this.moviePoster = _movieBean.readString();
        this.voteAverage = _movieBean.readString();
        this.plotSynopsis = _movieBean.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(moviePoster);
        parcel.writeString(voteAverage);
        parcel.writeString(plotSynopsis);

    }


    public static final Parcelable.Creator<MovieBean> CREATOR = new Parcelable.Creator<MovieBean>() {
        public MovieBean createFromParcel(Parcel movieBean) {
            return new MovieBean(movieBean);
        }

        public MovieBean[] newArray(int size) {
            return new MovieBean[size];
        }
    };

}

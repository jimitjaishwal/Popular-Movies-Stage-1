package com.example.jimitjaishwal.movieplex;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jimit Jaishwal on 3/6/2016.
 */
public class MovieData implements Parcelable {
    public String Base_Url_of_Poster;
    public String poster_path;
    public String original_title;
    public String overview;
    public String release_date;
    public String vote_average;
    public String backdrop_path;

    public MovieData(String base_Url_of_Poster,
                 String poster_path, String original_title,
                 String overview, String release_date,
                 String vote_average, String backdrop_path) {
        this.Base_Url_of_Poster = base_Url_of_Poster;
        this.poster_path = poster_path;
        this.original_title = original_title;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.backdrop_path = backdrop_path;
    }

    protected MovieData(Parcel in) {
        Base_Url_of_Poster = in.readString();
        poster_path = in.readString();
        original_title = in.readString();
        overview = in.readString();
        release_date = in.readString();
        vote_average = in.readString();
        backdrop_path = in.readString();
    }

    public static final Creator<MovieData> CREATOR = new Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Base_Url_of_Poster);
        dest.writeString(poster_path);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(vote_average);
        dest.writeString(backdrop_path);
    }
}

package com.example.jimitjaishwal.movieplex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jimit Jaishwal on 3/6/2016.
 */
public class MovieAdapter extends ArrayAdapter<MovieData> {
MovieData movieData;

    public MovieAdapter(Context context,List<MovieData> movieList) {
        super(context,0,movieList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        movieData = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_poster,parent,false);
        }

        String posterUrl = movieData.Base_Url_of_Poster + movieData.poster_path;
        ImageView imageView = (ImageView)convertView.findViewById(R.id.movie_poster_imageView);
        Picasso.with(getContext()).load(posterUrl).placeholder(R.drawable.loading).resize(185,278).into(imageView);
        return convertView;
    }
}

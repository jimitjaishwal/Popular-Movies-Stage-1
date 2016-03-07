package com.example.jimitjaishwal.movieplex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    public DetailsActivityFragment() {
    }

    @Bind(R.id.original_title)
    TextView textView;
    @Bind(R.id.poster)
    ImageView imageView;
    @Bind(R.id.year)
    TextView textView1;
    @Bind(R.id.rating)
    TextView textView2;
    @Bind(R.id.overview)
    TextView textView3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        Intent intent = getActivity().getIntent();
        MovieData mData = (MovieData) intent.getExtras().getParcelable("MovieData");
        ButterKnife.bind(this,rootView);

        textView.setText(mData.original_title);
        textView1.setText(mData.release_date.substring(0,4));
        textView2.setText(mData.vote_average + "/10");
        textView3.setText(mData.overview);

        String posterUrl = mData.Base_Url_of_Poster + mData.poster_path;
        Picasso.with(getActivity()).load(posterUrl).into(imageView);

        return rootView;
    }
}

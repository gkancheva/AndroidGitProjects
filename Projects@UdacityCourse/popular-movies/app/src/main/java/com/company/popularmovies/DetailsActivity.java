package com.company.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.popularmovies.models.Movie;
import com.company.popularmovies.util.StringUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.tv_movie_title) TextView mTvMovieTitle;
    @BindView(R.id.iv_poster) ImageView mIVPoster;
    @BindView(R.id.tv_release_date) TextView mTvReleaseDate;
    @BindView(R.id.tv_rating) TextView mTvRating;
    @BindView(R.id.tv_overview) TextView mTvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent() == null) {
            closeOnError();
            return;
        }

        Movie movie = getIntent().getParcelableExtra(getString(R.string.selected_movie_key));
        if(movie == null) {
            closeOnError();
            return;
        }

        this.populateUI(movie);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateUI(Movie movie) {
        Picasso.with(this)
                .load(movie.getThumbnail())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(this.mIVPoster);
        this.mTvMovieTitle.setText(movie.getOriginalName());
        this.mTvReleaseDate.setText(StringUtil.formatDate(movie.getReleaseDate()));
        this.mTvRating.setText(StringUtil.formatRating(movie.getRating()));
        this.mTvOverview.setText(movie.getOverview());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
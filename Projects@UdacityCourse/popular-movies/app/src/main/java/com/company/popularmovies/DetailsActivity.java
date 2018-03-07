package com.company.popularmovies;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.popularmovies.models.Movie;
import com.company.popularmovies.repository.MovieRepository;
import com.company.popularmovies.repository.MovieRepositoryImpl;
import com.company.popularmovies.services.MovieRepoListener;
import com.company.popularmovies.util.StringUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity
        implements View.OnClickListener, MovieRepoListener {

    @BindView(R.id.iv_movie_title) TextView mIvMoviePoster;
    @BindView(R.id.iv_poster) ImageView mIVPoster;
    @BindView(R.id.tv_release_date) TextView mTvReleaseDate;
    @BindView(R.id.tv_rating) TextView mTvRating;
    @BindView(R.id.tv_overview) TextView mTvOverview;
    @BindView(R.id.btn_favourites) ImageButton mBtnFavourite;
    private Movie mMovie;
    private MovieRepository mMovieRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mMovieRepo = new MovieRepositoryImpl(this, this, getSupportLoaderManager());
        this.mBtnFavourite.setOnClickListener(this);

        if(getIntent() == null) {
            closeOnError();
            return;
        }

        this.mMovie = getIntent().getParcelableExtra(getString(R.string.selected_movie_key));
        if(this.mMovie == null) {
            closeOnError();
            return;
        }

        this.populateUI(this.mMovie);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_favourites) {
            boolean isFav = this.mMovieRepo.isFavourite(this.mMovie.getId());
            if(!isFav) {
                this.mMovieRepo.addToFavourites(this.mMovie);
                return;
            }
            this.mMovieRepo.removeFromFavourite(this.mMovie.getId());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMoviesSuccess(List<Movie> movies) {
        boolean isFavourite = this.mMovieRepo.isFavourite(movies.get(0).getId());
        this.updateFavouriteBtn(isFavourite);
        String message = StringUtil.formatMessage(getString(R.string.message_format_movie_successfully_added_to_fav), movies.get(0).getOriginalName());
        if(!isFavourite) {
            message = StringUtil.formatMessage(getString(R.string.message_format_movie_successfully_deleted_from_fav), movies.get(0).getOriginalName());
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMoviesFailure() {
        Toast.makeText(this, getString(R.string.detail_error_message), Toast.LENGTH_SHORT).show();
    }

    private void updateFavouriteBtn(boolean isFavourite) {
        if(!isFavourite) {
            this.mBtnFavourite.setBackgroundResource(R.drawable.ic_favorite_24dp);
        } else {
            this.mBtnFavourite.setBackgroundResource(R.drawable.ic_un_favorite_24dp);
        }
    }

    private void populateUI(Movie movie) {
        this.mIvMoviePoster.setText(movie.getOriginalName());
        this.mTvReleaseDate.setText(StringUtil.formatDate(movie.getReleaseDate()));
        this.mTvRating.setText(StringUtil.formatRating(movie.getRating()));
        this.mTvOverview.setText(movie.getOverview());
        boolean isFavourite = this.mMovieRepo.isFavourite(movie.getId());
        this.updateFavouriteBtn(isFavourite);
        if(movie.getThumbnailPath() == null) {
            this.mIVPoster.setImageBitmap(movie.getThumbnail());
            return;
        }
        Picasso.with(this)
                .load(movie.getThumbnailPath())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(this.mIVPoster);

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
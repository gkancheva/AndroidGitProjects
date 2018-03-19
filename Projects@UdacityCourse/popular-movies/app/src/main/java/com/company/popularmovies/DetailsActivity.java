package com.company.popularmovies;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.popularmovies.models.Movie;
import com.company.popularmovies.models.Review;
import com.company.popularmovies.models.Trailer;
import com.company.popularmovies.repository.MovieRepository;
import com.company.popularmovies.repository.MovieRepositoryImpl;
import com.company.popularmovies.services.MovieRepoListener;
import com.company.popularmovies.services.ReviewClickListener;
import com.company.popularmovies.services.TrailerClickListener;
import com.company.popularmovies.services.TrailerReviewRepoListener;
import com.company.popularmovies.util.StringUtil;
import com.company.popularmovies.views.ItemDivider;
import com.company.popularmovies.views.ReviewRVAdapter;
import com.company.popularmovies.views.TrailerRVAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity
        implements View.OnClickListener,
        MovieRepoListener, TrailerReviewRepoListener,
        TrailerClickListener, ReviewClickListener {

    @BindView(R.id.iv_movie_heading) ImageView mIvMoviePoster;
    @BindView(R.id.iv_poster) ImageView mIVPoster;
    @BindView(R.id.tv_release_date) TextView mTvReleaseDate;
    @BindView(R.id.tv_rating) TextView mTvRating;
    @BindView(R.id.tv_overview) TextView mTvOverview;
    @BindView(R.id.btn_favourites) ImageButton mBtnFavourite;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mToolbar;
    @BindView(R.id.rv_trailers) RecyclerView mRVTrailers;
    @BindView(R.id.rv_reviews) RecyclerView mRVReviews;

    private Movie mMovie;
    private MovieRepository mMovieRepo;
    private TrailerRVAdapter mTrailerAdapter;
    private ReviewRVAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mMovieRepo = new MovieRepositoryImpl(this, this, this, getSupportLoaderManager());
        this.mBtnFavourite.setOnClickListener(this);
        this.setLayoutElements();

        if(getIntent() == null) {
            closeOnError();
            return;
        }

        this.mMovie = getIntent().getParcelableExtra(getString(R.string.selected_movie_key));
        if(this.mMovie == null) {
            closeOnError();
            return;
        }
        this.mMovieRepo.getTrailers(this.mMovie.getId());
        this.mMovieRepo.getReviews(this.mMovie.getId());
        this.populateUI(this.mMovie);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_favourites) {
            boolean isFav = this.mMovieRepo.isFavourite(this.mMovie);
            if(!isFav) {
                this.mMovieRepo.addToFavourites(this.mMovie);
                return;
            }
            this.mMovieRepo.removeFromFavourite(this.mMovie);
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
        boolean isFavourite = this.mMovieRepo.isFavourite(movies.get(0));
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
    public void onMoviesFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTrailersSuccess(List<Trailer> trailers) {
        this.mTrailerAdapter.updateListItems(trailers);
    }

    @Override
    public void onReviewsSuccess(List<Review> reviews) {
        this.mReviewAdapter.updateListItems(reviews);
    }

    @Override
    public void onReviewClicked(Review review) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(review.getAuthor());
        builder.setMessage(review.getContent());
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onTrailerClicked(Trailer trailer) {
        Uri youtubeUri = Uri.parse("vnd.youtube:" + trailer.getYoutubeKey());
        Uri webUri =  Uri.parse(trailer.getYoutubePath());
        Intent appIntent = new Intent(Intent.ACTION_VIEW, youtubeUri);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
        if(appIntent.resolveActivity(getPackageManager()) != null){
            this.startActivity(appIntent);
            return;
        }
        if(webIntent.resolveActivity(getPackageManager()) != null) {
            this.startActivity(webIntent);
        }
    }

    private void updateFavouriteBtn(boolean isFavourite) {
        if(!isFavourite) {
            this.mBtnFavourite.setBackgroundResource(R.drawable.ic_favorite_48dp);
        } else {
            this.mBtnFavourite.setBackgroundResource(R.drawable.ic_un_favorite_48dp);
        }
    }

    private void populateUI(Movie movie) {
        this.mToolbar.setTitle(movie.getOriginalName());
        this.mTvReleaseDate.setText(StringUtil.formatDate(movie.getReleaseDate()));
        this.mTvRating.setText(StringUtil.formatRating(movie.getRating()));
        this.mTvOverview.setText(movie.getOverview());
        boolean isFavourite = this.mMovieRepo.isFavourite(movie);
        this.updateFavouriteBtn(isFavourite);
        if(movie.getThumbnailPath() == null) {
            this.mIvMoviePoster.setImageBitmap(movie.getThumbnail());
            this.mIVPoster.setImageBitmap(movie.getThumbnail());
            return;
        }
        Picasso.with(this)
                .load(movie.getThumbnailPath())
                .placeholder(R.drawable.the_movie_db_logo)
                .error(R.drawable.the_movie_db_logo)
                .into(this.mIVPoster);
        Picasso.with(this)
                .load(movie.getThumbnailPath())
                .placeholder(R.drawable.the_movie_db_logo)
                .error(R.drawable.the_movie_db_logo)
                .into(this.mIvMoviePoster);
    }

    private void setLayoutElements() {
        this.mRVTrailers.addItemDecoration(new ItemDivider(this));
        this.mRVTrailers.setItemAnimator(new DefaultItemAnimator());
        this.mRVTrailers.setHasFixedSize(true);
        this.mRVTrailers.setLayoutManager(new LinearLayoutManager(this));
        this.mTrailerAdapter = new TrailerRVAdapter(this);
        this.mRVTrailers.setAdapter(this.mTrailerAdapter);

        this.mRVReviews.addItemDecoration(new ItemDivider(this));
        this.mRVReviews.setItemAnimator(new DefaultItemAnimator());
        this.mRVReviews.setHasFixedSize(true);
        this.mRVReviews.setLayoutManager(new LinearLayoutManager(this));
        this.mReviewAdapter = new ReviewRVAdapter(this);
        this.mRVReviews.setAdapter(this.mReviewAdapter);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
package com.company.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.company.popularmovies.models.Movie;
import com.company.popularmovies.repository.MovieRepository;
import com.company.popularmovies.repository.MovieRepositoryImpl;
import com.company.popularmovies.services.MovieClickListener;
import com.company.popularmovies.services.MovieRepoListener;
import com.company.popularmovies.views.MovieRVAdapter;
import com.company.popularmovies.views.ScrollListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        MovieClickListener, MovieRepoListener {

    private static final int NB_COLUMNS = 2;
    private static final int NB_COLUMNS_LAND = 3;
    private static final String ORDER_KEY = "ORDER";
    private static final String RV_POSITION = "RV_POSITION";
    private static final String POPULAR_ORDER = "popular";
    private static final String TOP_RATED_ORDER = "top_rated";
    private static final String FAVOURITES = " favourites";
    private MovieRVAdapter mMovieAdapter;
    private MovieRepository mMovieRepo;
    private String mOrder;
    private boolean mOrderHasChanged = false;
    private int mRVPosition = -1;

    @BindView(R.id.rv_movies) RecyclerView mRvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.mMovieRepo = new MovieRepositoryImpl(this, this, getSupportLoaderManager());
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.mRvMovies.setLayoutManager(new GridLayoutManager(this, NB_COLUMNS));
        } else {
            this.mRvMovies.setLayoutManager(new GridLayoutManager(this, NB_COLUMNS_LAND));
        }
        if(savedInstanceState != null) {
            this.onRestoreInstanceState(savedInstanceState);
        }
        if(this.mOrder == null) {
            this.mOrder = POPULAR_ORDER;
        }
        this.setTitle();
        this.mRvMovies.setHasFixedSize(true);
        this.mMovieAdapter = new MovieRVAdapter(this, this);
        this.mRvMovies.setAdapter(this.mMovieAdapter);
        this.mMovieRepo.getMovies(this.mOrder);
        this.mRvMovies.addOnScrollListener(new ScrollListener() {
            @Override
            public void onScrollRequested() {
                if (!mOrder.equals(FAVOURITES)) {
                    mMovieRepo.getMovies(mOrder);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ORDER_KEY, this.mOrder);
        int position = ((GridLayoutManager)this.mRvMovies.getLayoutManager())
                .findFirstVisibleItemPosition();
        outState.putInt(RV_POSITION, position);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mOrder = savedInstanceState.getString(ORDER_KEY);
        this.mRVPosition = savedInstanceState.getInt(RV_POSITION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(this.mOrder.equals(FAVOURITES)) {
            this.mMovieRepo.getFavourites(this.mOrder);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_top_rated:
                if(this.mOrder.equals(TOP_RATED_ORDER)) {
                    break;
                }
                this.mOrder = TOP_RATED_ORDER;
                this.mOrderHasChanged = true;
                this.setTitle();
                this.mMovieRepo.getMovies(this.mOrder);
                break;
            case R.id.action_show_popular:
                if(this.mOrder.equals(POPULAR_ORDER)) {
                    break;
                }
                this.mOrder = POPULAR_ORDER;
                this.mOrderHasChanged = true;
                this.setTitle();
                this.mMovieRepo.getMovies(this.mOrder);
                break;
            case R.id.action_favourites:
                if(this.mOrder.equals(FAVOURITES)) {
                    break;
                }
                this.mOrder = FAVOURITES;
                this.mOrderHasChanged = true;
                this.setTitle();
                this.mMovieRepo.getFavourites(this.mOrder);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(getString(R.string.selected_movie_key), movie);
        startActivity(intent);
    }

    @Override
    public void onMoviesSuccess(List<Movie> movies) {
        if(this.mOrderHasChanged || this.mOrder.equals(FAVOURITES)) {
            this.mMovieAdapter.updateMovieList(movies);
            this.mOrderHasChanged = false;
        } else {
            this.mMovieAdapter.addToList(movies);
        }
        if(this.mRVPosition != -1) {
            this.mRvMovies.getLayoutManager().scrollToPosition(this.mRVPosition);
        }
    }

    @Override
    public void onMoviesFailure() {
        Toast.makeText(this, getString(R.string.main_error_message), Toast.LENGTH_SHORT).show();
    }

    private void setTitle() {
        switch (this.mOrder) {
            case TOP_RATED_ORDER: setTitle(R.string.top_rated); break;
            case POPULAR_ORDER: setTitle(R.string.popular); break;
            case FAVOURITES: setTitle(R.string.favourites); break;
            default: break;
        }
    }
}
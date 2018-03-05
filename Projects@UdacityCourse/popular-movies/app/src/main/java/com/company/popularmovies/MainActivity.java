package com.company.popularmovies;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import com.company.popularmovies.util.MovieRVAdapter;
import com.company.popularmovies.util.ScrollListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        MovieClickListener, MovieRepoListener {

    private static final int NB_COLUMNS = 2;
    private static final String POPULAR_ORDER = "popular";
    private static final String TOP_RATED_ORDER = "top_rated";
    private MovieRVAdapter mMovieAdapter;
    private MovieRepository mMovieRepo;
    private String mOrder = POPULAR_ORDER;
    private boolean mOrderHasChanged = false;

    @BindView(R.id.rv_movies) RecyclerView mRvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.mMovieRepo = new MovieRepositoryImpl(this, this, getSupportLoaderManager());
        this.mRvMovies.setLayoutManager(new GridLayoutManager(this, NB_COLUMNS));
        this.mRvMovies.setHasFixedSize(true);
        this.mMovieAdapter = new MovieRVAdapter(this, this);
        this.mRvMovies.setAdapter(this.mMovieAdapter);
        this.mMovieRepo.getMovies(this.mOrder);
        this.mRvMovies.addOnScrollListener(new ScrollListener() {
            @Override
            public void onScrollRequested() {
                mMovieRepo.getMovies(mOrder);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_change_movie_order);
        if(this.mOrder.equals(POPULAR_ORDER)) {
            item.setTitle(R.string.top_rated);
        } else {
            item.setTitle(R.string.popular);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_change_movie_order) {
            invalidateOptionsMenu();
            this.mOrder = this.mOrder.equals(POPULAR_ORDER) ? TOP_RATED_ORDER : POPULAR_ORDER;
            this.mMovieRepo.getMovies(this.mOrder);
            this.mOrderHasChanged = true;
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
        if(this.mOrderHasChanged) {
            this.mMovieAdapter.updateMovieList(movies);
            this.mOrderHasChanged = false;
            return;
        }
        this.mMovieAdapter.addToList(movies);
    }

    @Override
    public void onMoviesFailure() {
        Toast.makeText(this, getString(R.string.detail_error_message), Toast.LENGTH_SHORT).show();
    }
}
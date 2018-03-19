package com.company.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
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
import com.company.popularmovies.views.MoviesViewModel;
import com.company.popularmovies.views.ScrollListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        MovieClickListener, MovieRepoListener {

    private static final int NB_COLUMNS = 2;
    private static final int NB_COLUMNS_LAND = 3;
    private static final String ORDER_KEY = "ORDER";
    private MovieRVAdapter mMovieAdapter;
    private MovieRepository mMovieRepo;
    private String mOrder;
    private boolean mOrderHasChanged = false;
    private MoviesViewModel mMovieViewModel;

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

        this.mMovieViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);

        if(savedInstanceState != null) {
            this.onRestoreInstanceState(savedInstanceState);
        }
        if(this.mOrder == null) {
            this.mOrder = getString(R.string.popular_order);
        }
        this.setTitle();
        this.setRecyclerView();

        if(this.mMovieViewModel.getMovies(this.mOrder).size() == 0) {
            this.mMovieRepo.getMovies(this.mOrder);
        } else {
            this.updateUi(this.mMovieViewModel.getMovies(this.mOrder));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ORDER_KEY, this.mOrder);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mOrder = savedInstanceState.getString(ORDER_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(this.mOrder.equals(getString(R.string.favourites_order))) {
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
                if(this.mOrder.equals(getString(R.string.top_rated_order))) {
                    break;
                }
                this.mOrder = getString(R.string.top_rated_order);
                this.mOrderHasChanged = true;
                this.setTitle();
                if(this.mMovieViewModel.getMovies(this.mOrder).size() == 0) {
                    this.mMovieRepo.getMovies(this.mOrder);
                } else {
                    this.updateUi(this.mMovieViewModel.getMovies(this.mOrder));
                }
                break;
            case R.id.action_show_popular:
                if(this.mOrder.equals(getString(R.string.popular_order))) {
                    break;
                }
                this.mOrder = getString(R.string.popular_order);
                this.mOrderHasChanged = true;
                this.setTitle();
                if(this.mMovieViewModel.getMovies(this.mOrder).size() == 0) {
                    this.mMovieRepo.getMovies(this.mOrder);
                } else {
                    this.updateUi(this.mMovieViewModel.getMovies(this.mOrder));
                }
                break;
            case R.id.action_favourites:
                if(this.mOrder.equals(getString(R.string.favourites_order))) {
                    break;
                }
                this.mOrder = getString(R.string.favourites_order);
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
        if(this.mOrderHasChanged) {
            this.mMovieViewModel.setMovies(movies, this.mOrder);
        } else {
            this.mMovieViewModel.addMovies(movies, this.mOrder);
        }
        this.updateUi(movies);
    }

    @Override
    public void onMoviesFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void updateUi(List<Movie> movies) {
        if(this.mOrderHasChanged || this.mOrder.equals(getString(R.string.favourites_order))) {
            this.mMovieAdapter.updateMovieList(movies);
            this.mOrderHasChanged = false;
        } else {
            this.mMovieAdapter.addToList(movies);
        }
    }

    private void setTitle() {
        if(this.mOrder.equals(getString(R.string.top_rated_order))) {
            setTitle(R.string.top_rated);
        } else if(this.mOrder.equals(getString(R.string.popular_order))) {
            setTitle(R.string.popular);
        } else {
            setTitle(R.string.favourites);
        }
    }

    private void setRecyclerView() {
        this.mRvMovies.setHasFixedSize(true);
        this.mMovieAdapter = new MovieRVAdapter(this, this);
        this.mRvMovies.setAdapter(this.mMovieAdapter);
        this.mRvMovies.addOnScrollListener(new ScrollListener() {
            @Override
            public void onScrollRequested() {
                if (!mOrder.equals(getString(R.string.favourites_order))) {
                    this.setTotal(mMovieViewModel.getMovies(mOrder).size());
                    mMovieRepo.fetchNewPage(mOrder, mMovieViewModel.getPage(mOrder) + 1);
                }
            }
        });
    }
}
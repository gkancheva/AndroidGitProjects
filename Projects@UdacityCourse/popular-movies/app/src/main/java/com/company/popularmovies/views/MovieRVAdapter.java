package com.company.popularmovies.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.company.popularmovies.R;
import com.company.popularmovies.models.Movie;
import com.company.popularmovies.services.MovieClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieRVAdapter extends RecyclerView.Adapter<MovieRVAdapter.MovieViewHolder> {

    private List<Movie> mMovies;
    private final Context mContext;
    private MovieClickListener mClickListener;

    public MovieRVAdapter(Context mContext, MovieClickListener listener) {
        this.mMovies = new ArrayList<>();
        this.mContext = mContext;
        this.mClickListener = listener;
    }

    @Override
    public MovieRVAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_poster_item, parent, false);
        view.setFocusable(true);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieRVAdapter.MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.mMovies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_movie_poster)
        ImageView mMoviePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            Movie movie = mMovies.get(position);
            if(movie.getThumbnailPath() == null) {
                this.mMoviePoster.setImageBitmap(movie.getThumbnail());
                return;
            }
            Picasso.with(mContext)
                    .load(movie.getThumbnailPath())
                    .placeholder(R.drawable.the_movie_db_logo)
                    .error(R.drawable.the_movie_db_logo)
                    .into(this.mMoviePoster);
        }

        @Override
        public void onClick(View view) {
            Movie movie = mMovies.get(getAdapterPosition());
            mClickListener.onMovieClicked(movie);
        }
    }

    public void updateMovieList(List<Movie> movies) {
        int itemCount = this.getItemCount();
        this.mMovies.clear();
        this.notifyItemRangeRemoved(0, itemCount);
        this.mMovies.addAll(movies);
        this.notifyItemRangeInserted(0, movies.size());
    }

    public void addToList(List<Movie> movies) {
        int positionStart = this.getItemCount();
        this.mMovies.addAll(movies);
        this.notifyItemRangeInserted(positionStart, movies.size());
    }
}
package com.company.bakingapp;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.bakingapp.models.Recipe;
import com.company.bakingapp.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentDetailView extends Fragment
        implements ExoPlayer.EventListener, View.OnClickListener {

    private static final String RECIPE = "RECIPE", STEP_INDEX = "STEP_INDEX";
    private static final String PLAYER_POSITION_KEY = "INDEX", PLAYER_STATE_KEY = "PLAYBACK_STATE";
    private Recipe mRecipe;
    private int mCurrentStepInd;
    private SimpleExoPlayer mExoPlayer;
    @BindView(R.id.tv_step_instruction) TextView mStepDescription;
    @BindView(R.id.player_view_step) SimpleExoPlayerView mPlayerView;
    @BindView(R.id.btn_previous) Button mBtnPreviousStep;
    @BindView(R.id.btn_next) Button mBtnNextStep;
    @BindView(R.id.iv_thumbnail)
    ImageView mThumbnail;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private long mPlayerPosition = -1;
    private int mPlaybackState = -1;

    public FragmentDetailView() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            this.mRecipe = savedInstanceState.getParcelable(RECIPE);
            this.mCurrentStepInd = savedInstanceState.getInt(STEP_INDEX);
            this.mPlayerPosition = savedInstanceState.getLong(PLAYER_POSITION_KEY);
            this.mPlaybackState = savedInstanceState.getInt(PLAYER_STATE_KEY);
        }
        if(getArguments() != null && getArguments().getParcelable(RECIPE) != null) {
            this.mRecipe = getArguments().getParcelable(RECIPE);
            this.mCurrentStepInd = getArguments().getInt(STEP_INDEX);
        }
        View view = inflater.inflate(R.layout.fragment_step_view, container, false);
        ButterKnife.bind(this, view);
        this.mBtnPreviousStep.setOnClickListener(this);
        this.mBtnNextStep.setOnClickListener(this);
        if(getResources().getBoolean(R.bool.isTablet)) {
            this.mBtnPreviousStep.setVisibility(View.GONE);
            this.mBtnNextStep.setVisibility(View.GONE);
        }
        this.initializeMediaSession();
        if(this.mRecipe != null && this.mRecipe.getSteps().get(this.mCurrentStepInd) != null) {
            this.setContent(this.mRecipe.getSteps().get(this.mCurrentStepInd));
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE, this.mRecipe);
        outState.putInt(STEP_INDEX, this.mCurrentStepInd);
        this.mPlayerPosition = this.mExoPlayer.getCurrentPosition();
        outState.putLong(PLAYER_POSITION_KEY, this.mPlayerPosition);
        this.mPlaybackState = this.mExoPlayer.getPlaybackState();
        outState.putInt(PLAYER_STATE_KEY, this.mPlaybackState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_previous:
                this.mCurrentStepInd--;
                if(this.mCurrentStepInd < 0) {
                    break;
                }
                this.setContent(this.mRecipe.getSteps().get(this.mCurrentStepInd));
                break;
            case R.id.btn_next:
                this.mCurrentStepInd++;
                if(this.mCurrentStepInd >= this.mRecipe.getSteps().size()) {
                    break;
                }
                this.setContent(this.mRecipe.getSteps().get(this.mCurrentStepInd));
                break;
            default: break;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {}

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

    @Override
    public void onLoadingChanged(boolean isLoading) {}

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            this.mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    this.mExoPlayer.getCurrentPosition(), 1f);
        } else if(playbackState == ExoPlayer.STATE_READY) {
            this.mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    this.mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(this.mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {}

    @Override
    public void onPositionDiscontinuity() {}

    public void setContent(Step step) {
        this.mStepDescription.setText(step.getDescription());
        if(!getResources().getBoolean(R.bool.isTablet)) {
            this.updateButtons();
        }
        if(!step.getThumbnailUrl().isEmpty()) {
            this.mThumbnail.setVisibility(View.VISIBLE);
            Picasso.with(getActivity())
                    .load(Uri.parse(step.getThumbnailUrl()))
                    .placeholder(R.drawable.dessert_vector)
                    .error(R.drawable.dessert_vector)
                    .into(this.mThumbnail);
        } else {
            this.mThumbnail.setVisibility(View.GONE);
        }
        if(!step.getVideoUrl().isEmpty()) {
            this.mPlayerView.setVisibility(View.VISIBLE);
            if(!getResources().getBoolean(R.bool.isTablet) &&
                    (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
                this.mPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                this.mPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                this.mPlayerView.setPadding(0, 0, 0, 0);
                this.mBtnNextStep.setVisibility(View.GONE);
                this.mBtnPreviousStep.setVisibility(View.GONE);
            }
            this.initializePlayer(Uri.parse(step.getVideoUrl()));
        } else {
            this.mPlayerView.setVisibility(View.GONE);
        }
    }

    private void updateButtons() {
        if(this.mCurrentStepInd == 0) {
            this.mBtnPreviousStep.setEnabled(false);
        } else {
            this.mBtnPreviousStep.setEnabled(true);
        }
        if(this.mCurrentStepInd == this.mRecipe.getSteps().size() -1) {
            this.mBtnNextStep.setEnabled(false);
        } else {
            this.mBtnNextStep.setEnabled(true);
        }
    }

    private void initializePlayer(Uri mediaUri) {
        String userAgent = FragmentDetailView.class.getSimpleName();
        if(this.mExoPlayer == null) {
            this.mExoPlayer = ExoPlayerFactory
                    .newSimpleInstance(getActivity(),
                            new DefaultTrackSelector(),
                            new DefaultLoadControl());
            this.mPlayerView.setPlayer(this.mExoPlayer);
        } else {
            this.mExoPlayer.stop();
        }
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
        if(this.mPlayerPosition != -1) {
            this.mExoPlayer.seekTo(this.mPlayerPosition);
        }
        this.mExoPlayer.prepare(mediaSource);
        boolean playWhenReady = this.mPlaybackState == 3;
        this.mExoPlayer.setPlayWhenReady(playWhenReady);
    }

    private void initializeMediaSession() {
        String tag = FragmentDetailView.class.getSimpleName();
        mMediaSession = new MediaSessionCompat(getActivity(), tag);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                mExoPlayer.setPlayWhenReady(false);
            }

            @Override
            public void onPause() {
                mExoPlayer.setPlayWhenReady(false);
            }

            @Override
            public void onSkipToPrevious() {
                mExoPlayer.seekTo(0);
            }
        });
        mMediaSession.setActive(true);
    }


    @Override
    public void onStart() {
        super.onStart();
        if(this.mRecipe != null && this.mCurrentStepInd != -1) {
            this.setContent(mRecipe.getSteps().get(this.mCurrentStepInd));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.releasePlayer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.releasePlayer();
    }

    private void releasePlayer() {
        if(this.mExoPlayer != null) {
            this.mExoPlayer.stop();
            this.mExoPlayer.release();
            this.mExoPlayer = null;
        }
    }
}
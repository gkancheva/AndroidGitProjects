package com.company.bakingapp;

import android.content.res.Configuration;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentDetailView extends Fragment
        implements ExoPlayer.EventListener {

    private Step mStep;
    private SimpleExoPlayer mExoPlayer;
    @BindView(R.id.tv_step_instruction) TextView mStepDescription;
    @BindView(R.id.player_view_step) SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    public FragmentDetailView() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            this.mStep = savedInstanceState.getParcelable("STEP");
        }
        if(getArguments() != null && getArguments().getParcelable("STEP") != null) {
            this.mStep = getArguments().getParcelable("STEP");
        }
        View view = inflater.inflate(R.layout.fragment_step_view, container, false);
        ButterKnife.bind(this, view);
        this.initializeMediaSession();
        if(this.mStep != null) {
            this.setContent(this.mStep);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("STATE", FragmentDetailView.class.getSimpleName());
    }

    public void setContent(Step step) {
        this.mStepDescription.setText(step.getDescription());
        if(!step.getVideoUrl().isEmpty()) {
            this.mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(
                    getResources(), R.drawable.dessert_vector));
            this.mPlayerView.setVisibility(View.VISIBLE);
            if(!getResources().getBoolean(R.bool.isTablet) &&
                    (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
                this.mPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                this.mPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            }
            this.initializePlayer(Uri.parse(step.getVideoUrl()));
        } else {
            this.mPlayerView.setVisibility(View.GONE);
        }
    }

    private void initializePlayer(Uri mediaUri) {
        if(this.mExoPlayer == null) {
            this.mExoPlayer = ExoPlayerFactory
                    .newSimpleInstance(getActivity(),
                            new DefaultTrackSelector(),
                            new DefaultLoadControl());
            this.mPlayerView.setPlayer(this.mExoPlayer);
            String userAgent = "User agent";
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            this.mExoPlayer.prepare(mediaSource);
            this.mExoPlayer.setPlayWhenReady(true);
        } else {
            this.mExoPlayer.stop();
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), "User agent"), new DefaultExtractorsFactory(), null, null);
            this.mExoPlayer.prepare(mediaSource);
            this.mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getActivity(), "MediaSession");
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
                mExoPlayer.setPlayWhenReady(true);
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

    private void releasePlayer() {
        if(this.mExoPlayer != null) {
            this.mExoPlayer.stop();
            this.mExoPlayer.release();
            this.mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.releasePlayer();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

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
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
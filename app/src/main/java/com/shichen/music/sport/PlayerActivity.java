package com.shichen.music.sport;

import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.shichen.music.R;
import com.shichen.music.basic.BaseActivity;
import com.shichen.music.basic.Viewable;
import com.shichen.music.sport.contract.PlayerContract;
import com.shichen.music.sport.presenter.PlayerActivityPresenter;

import butterknife.BindView;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/3/29
 */
@Viewable(layout = R.layout.activity_player,presenter = PlayerActivityPresenter.class)
public class PlayerActivity extends BaseActivity<PlayerContract.View,PlayerActivityPresenter> implements PlayerContract.View{
    @BindView(R.id.player_view)
    PlayerView mPlayerView;
    @Override
    public void init() {
        Uri musicUri= Uri.parse("http://ws.stream.qqmusic.qq.com/C4000003SNcL1xzzAB.m4a?fromtag=0&guid=126548448&vkey=4D60915F37D691D3253639B93C7B1077AD0042EA761E6C3A468B2582F529306573DE4AB78802C5738F45126D22BF06ACF28561644378A590");
        SimpleExoPlayer exoPlayer= ExoPlayerFactory.newSimpleInstance(this);
        mPlayerView.setPlayer(exoPlayer);
        exoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
                Log.d(TAG,"onTimelineChanged");
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.d(TAG,"onTracksChanged");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.d(TAG,"onLoadingChanged");
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.d(TAG,"onPlayerStateChanged");
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                Log.d(TAG,"onRepeatModeChanged");
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                Log.d(TAG,"onShuffleModeEnabledChanged");
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.d(TAG,"onPlayerError");
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                Log.d(TAG,"onPositionDiscontinuity");
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                Log.d(TAG,"onPlaybackParametersChanged");
            }

            @Override
            public void onSeekProcessed() {
                Log.d(TAG,"onSeekProcessed");
            }
        });
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "QQMusic"));
// This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(musicUri);
// Prepare the player with the source.
        exoPlayer.prepare(videoSource);
    }
}

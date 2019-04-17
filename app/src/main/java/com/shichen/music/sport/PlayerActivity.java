package com.shichen.music.sport;

import android.Manifest;
import android.media.audiofx.Visualizer;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.View;

import com.google.android.exoplayer2.ControlDispatcher;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.analytics.AnalyticsCollector;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.audio.AudioListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Util;
import com.shichen.music.R;
import com.shichen.music.basic.BaseActivity;
import com.shichen.music.basic.Viewable;
import com.shichen.music.sport.contract.PlayerContract;
import com.shichen.music.sport.presenter.PlayerActivityPresenter;
import com.shichen.music.utils.StatusUtil;
import com.shichen.music.widget.LyricsView;
import com.shichen.music.widget.WaveSurfaceView;

import butterknife.BindView;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/3/29
 */
@Viewable(layout = R.layout.activity_player, presenter = PlayerActivityPresenter.class, needPermissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS})
public class PlayerActivity extends BaseActivity<PlayerContract.View, PlayerActivityPresenter> implements PlayerContract.View {
    @BindView(R.id.player_control_view)
    PlayerControlView mPlayerView;
    @BindView(R.id.lyrics_view)
    LyricsView mLyricsView;
    SimpleExoPlayer exoPlayer;
    //获取音频波形的类
    Visualizer mVisualizer;
    @BindView(R.id.wave_surface)
    WaveSurfaceView mWaveSurfaceView;

    @Override
    public void init() {
        StatusUtil.setUseStatusBarColor(this, 0xff000000);
        StatusUtil.setSystemStatus(this, false, false);
        presenter.setBundle(getIntent().getExtras());
    }

    @Override
    public void initPlayer() {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this);
        mPlayerView.setPlayer(exoPlayer);
        exoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
                Log.d(TAG, "onTimelineChanged - > timeline " + timeline.getPeriodCount());
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.d(TAG, "onTracksChanged - currentPeriodInWindow = " + exoPlayer.getCurrentPeriodIndex());
                //presenter.setCurrentPeriod(exoPlayer.getCurrentPeriodIndex());
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.d(TAG, "onLoadingChanged - > isLoading = " + isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                /**
                 * The player does not have any media to play.
                 * 没有可用的多媒体资源

                 //int STATE_IDLE = 1;
                 *
                 * The player is not able to immediately play from its current position. This state typically
                 * occurs when more data needs to be loaded.
                 * 多媒体资源正在缓冲

                 //int STATE_BUFFERING = 2;
                 *
                 * The player is able to immediately play from its current position. The player will be playing if
                 * {@link #getPlayWhenReady()} is true, and paused otherwise.
                 * 多媒体资源缓冲完成

                 //int STATE_READY = 3;
                 *
                 * The player has finished playing the media.
                 * 多媒体资源播放结束*/

                //int STATE_ENDED = 4;
                Log.d(TAG, "onPlayerStateChanged and getLyrics - > playWhenReady = " + playWhenReady + " ; playbackState = " + playbackState);
                switch (playbackState) {
                    case Player.STATE_IDLE:
                        break;
                    case Player.STATE_BUFFERING:
                        break;
                    case Player.STATE_READY:
                        break;
                    case Player.STATE_ENDED:
                        /*if (mVisualizer != null) {
                            mVisualizer.setEnabled(false);
                        }*/
                        break;

                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                Log.d(TAG, "onRepeatModeChanged");
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                Log.d(TAG, "onShuffleModeEnabledChanged");
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.d(TAG, "onPlayerError");
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                Log.d(TAG, "onPositionDiscontinuity");
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                Log.d(TAG, "onPlaybackParametersChanged");
            }

            @Override
            public void onSeekProcessed() {
                Log.d(TAG, "onSeekProcessed");
            }
        });
        mPlayerView.setControlDispatcher(new ControlDispatcher() {
            @Override
            public boolean dispatchSetPlayWhenReady(Player player, boolean playWhenReady) {
                player.setPlayWhenReady(playWhenReady);
                if (player.getPlaybackState() == Player.STATE_READY) {
                    mLyricsView.start();
                }
                return false;
            }

            @Override
            public boolean dispatchSeekTo(Player player, int windowIndex, long positionMs) {
                player.seekTo(windowIndex, positionMs);
                if (player.getPlaybackState() == Player.STATE_READY) {
                    mLyricsView.seekTo();
                }
                return false;
            }

            @Override
            public boolean dispatchSetRepeatMode(Player player, int repeatMode) {
                player.setRepeatMode(repeatMode);
                return false;
            }

            @Override
            public boolean dispatchSetShuffleModeEnabled(Player player, boolean shuffleModeEnabled) {
                player.setShuffleModeEnabled(shuffleModeEnabled);
                return false;
            }

            @Override
            public boolean dispatchStop(Player player, boolean reset) {
                player.stop(reset);
                return false;
            }
        });
        mPlayerView.show();
        mPlayerView.setShowTimeoutMs(-1);
        mLyricsView.linkPlayer(exoPlayer);
        mLyricsView.setmLyricsOnTouchListener(new LyricsView.LyricsOnTouchListener() {
            @Override
            public void onTouch() {

            }
        });
        //exoPlayer.getAudioSessionId();
        exoPlayer.addAudioListener(new AudioListener() {
            @Override
            public void onAudioSessionId(int audioSessionId) {
                Log.d(TAG, "onAudioSessionId = " + audioSessionId);
                //exoPlayer.setPlayWhenReady(true);
                setVisualizer(audioSessionId);
            }

            @Override
            public void onAudioAttributesChanged(AudioAttributes audioAttributes) {

            }

            @Override
            public void onVolumeChanged(float volume) {

            }
        });
    }

    @Override
    public int getCurrentPeriodIndex() {
        if (exoPlayer != null) {
            return exoPlayer.getCurrentPeriodIndex();
        }
        return 0;
    }

    @Override
    public void setMediaSource(MediaSource mediaSource) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                exoPlayer.prepare(mediaSource);
            }
        });
    }

    private void setVisualizer(int audioSessionId) {
        mVisualizer = new Visualizer(audioSessionId);
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                Log.d(TAG, "onWaveFormDataCapture - > waveform.length() = " + waveform.length);
                mWaveSurfaceView.updateWave(waveform);
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                Log.d(TAG, "onFftDataCapture - > fft.length() = " + fft.length);
            }
        }, Visualizer.getMaxCaptureRate() / 2, true, true);
        mVisualizer.setEnabled(true);
    }

    @Override
    public void setLyrics(String lyrics) {
        mLyricsView.setLyrics(lyrics);
    }

    @Override
    public void tryNext() {
        if (exoPlayer != null) {
            if (exoPlayer.hasNext()) {
                exoPlayer.next();
                exoPlayer.retry();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (exoPlayer != null) {
            exoPlayer.stop();
        }
        //mVisualizer.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) exoPlayer.release();
        //mVisualizer.release();
    }
}

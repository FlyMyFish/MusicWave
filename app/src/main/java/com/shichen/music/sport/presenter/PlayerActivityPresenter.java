package com.shichen.music.sport.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.common.base.Optional;
import com.shichen.music.basic.BasePresenter;
import com.shichen.music.data.SongTokenItemBean;
import com.shichen.music.data.source.SongTokenItemBeanRepository;
import com.shichen.music.data.source.local.QQMusicDatabase;
import com.shichen.music.data.source.local.SongTokenItemBeanLocalSource;
import com.shichen.music.data.source.remote.SongTokenItemBeanRemoteSource;
import com.shichen.music.sport.contract.PlayerContract;
import com.shichen.music.utils.LogUtils;
import com.shichen.music.utils.PlayListUtil;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/3/29
 */
public class PlayerActivityPresenter extends BasePresenter<PlayerContract.View> implements PlayerContract.Presenter {
    private SongTokenItemBeanRepository mSongTokenItemBeanRepository;
    private PlayListUtil mPlayListUtil;
    private List<String> playList;
    private int currentPeriod;

    @Override
    public void setBundle(Bundle bundle) {
        QQMusicDatabase database = QQMusicDatabase.getInstance(view.getContext());
        mSongTokenItemBeanRepository = SongTokenItemBeanRepository.getInstance(SongTokenItemBeanLocalSource.getInstance(database.songTokenItemDao()), SongTokenItemBeanRemoteSource.getInstance());
        mPlayListUtil = PlayListUtil.getInstance(view.getContext());
        lyricsDisposable = new CompositeDisposable();
        view.initPlayer();
        getToken();
    }

    private void getToken() {
        playList = mPlayListUtil.getPlayList();
        if (playList.size() > 0) {
            view.unSubscribe();
            Disposable disposable = Observable.fromIterable(playList)
                    .map(s ->
                            mSongTokenItemBeanRepository.getTokenByMid(s)
                                    .subscribe(songTokenItemBean -> getSuccess(songTokenItemBean), throwable -> view.onFailed(null, throwable)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(disposableItem -> view.subscribe(disposableItem));
            view.subscribe(disposable);
        }
    }

    private void getSuccess(Optional<SongTokenItemBean> songTokenItemBeanOptional) {
        if (songTokenItemBeanOptional.isPresent()) {
            SongTokenItemBean songTokenItemBean = songTokenItemBeanOptional.get();
            addMediaSource(createSource(songTokenItemBean));
            //刷新界面
        }
    }

    private MediaSource createSource(SongTokenItemBean songTokenItemBean) {
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(view.getContext(),
                Util.getUserAgent(view.getContext(), "QQMusic"));
        // This is the MediaSource representing the media to be played.
        LogUtils.Log(TAG, "realUrl - > " + songTokenItemBean.getRealUrl());
        return new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(UriUtil.resolveToUri(songTokenItemBean.getRealUrl(), ""));
    }

    private ConcatenatingMediaSource mConcatenatingMediaSource;

    private void addMediaSource(MediaSource mediaSource) {
        mediaSource.addEventListener(new Handler(Looper.getMainLooper()), new MediaSourceEventListener() {
            @Override
            public void onMediaPeriodCreated(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

            }

            @Override
            public void onMediaPeriodReleased(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

            }

            @Override
            public void onLoadStarted(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
                LogUtils.Log(TAG, "mediaSource: onLoadStarted -> windowIndex = " + windowIndex + " mediaPeriodId = " + mediaPeriodId.adIndexInAdGroup);
            }

            @Override
            public void onLoadCompleted(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
                LogUtils.Log(TAG, "mediaSource: onLoadCompleted -> windowIndex = " + windowIndex);
            }

            @Override
            public void onLoadCanceled(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

            }

            @Override
            public void onLoadError(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException error, boolean wasCanceled) {
                LogUtils.Log(TAG, "mediaSource: onLoadError -> windowIndex = " + windowIndex);
                //如果mediaSource报错，则直接播放下一曲
                view.tryNext();
            }

            @Override
            public void onReadingStarted(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
                LogUtils.Log(TAG, "mediaSource: onReadingStarted -> windowIndex = " + windowIndex);
                //延时1秒获取periodIndex
                Disposable disposable = Observable.just(0).delay(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(integer -> setCurrentPeriod(view.getCurrentPeriodIndex()));
            }

            @Override
            public void onUpstreamDiscarded(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {

            }

            @Override
            public void onDownstreamFormatChanged(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {

            }
        });
        if (mConcatenatingMediaSource == null) {
            mConcatenatingMediaSource = new ConcatenatingMediaSource(mediaSource);
            view.setMediaSource(mConcatenatingMediaSource);
        } else {
            mConcatenatingMediaSource.addMediaSource(mediaSource);
        }
    }

    @Override
    public void setCurrentPeriod(int currentPeriod) {
        this.currentPeriod = currentPeriod;
        //getLyrics();
    }

    @Override
    public void getLyrics() {
        if (playList.size() > 0) {
            lyricsDisposable.clear();
            LogUtils.Log(TAG, "getLyrics: " + currentPeriod);
            lyricsDisposable.add(mSongTokenItemBeanRepository.getLyrics(playList.get(currentPeriod))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(stringOptional -> view.setLyrics(stringOptional.get()), throwable -> view.onFailed(null, throwable)));
        }
    }

    private CompositeDisposable lyricsDisposable;
}

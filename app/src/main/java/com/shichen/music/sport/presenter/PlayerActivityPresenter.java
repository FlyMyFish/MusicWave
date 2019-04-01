package com.shichen.music.sport.presenter;

import android.os.Bundle;
import android.util.Log;

import com.google.common.base.Optional;
import com.shichen.music.basic.BasePresenter;
import com.shichen.music.data.SongTokenItemBean;
import com.shichen.music.data.source.SongTokenItemBeanRepository;
import com.shichen.music.data.source.local.QQMusicDatabase;
import com.shichen.music.data.source.local.SongTokenItemBeanLocalSource;
import com.shichen.music.data.source.remote.SongTokenItemBeanRemoteSource;
import com.shichen.music.sport.PlayerActivity;
import com.shichen.music.sport.contract.PlayerContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/3/29
 */
public class PlayerActivityPresenter extends BasePresenter<PlayerContract.View> implements PlayerContract.Presenter {
    private String songMid;
    private SongTokenItemBeanRepository mSongTokenItemBeanRepository;

    @Override
    public void setBundle(Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(PlayerActivity.SONG_MID)) {
                songMid = bundle.getString(PlayerActivity.SONG_MID);
                QQMusicDatabase database = QQMusicDatabase.getInstance(view.getContext());
                mSongTokenItemBeanRepository = SongTokenItemBeanRepository.getInstance(SongTokenItemBeanLocalSource.getInstance(database.songTokenItemDao()), SongTokenItemBeanRemoteSource.getInstance());
                getToken();
            }
        }
    }

    private void getToken(){
        view.unSubscribe();
        Disposable disposable=mSongTokenItemBeanRepository.getTokenByMid(songMid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songTokenItemBean -> getSuccess(songTokenItemBean), throwable -> view.onFailed(null,throwable));
        view.subscribe(disposable);
    }

    private void getSuccess(Optional<SongTokenItemBean> songTokenItemBeanOptional){
        if (songTokenItemBeanOptional.isPresent()){
            SongTokenItemBean songTokenItemBean=songTokenItemBeanOptional.get();
            view.initPlayer(songTokenItemBean.getFilename(),songTokenItemBean.getVkey());
        }
    }
}

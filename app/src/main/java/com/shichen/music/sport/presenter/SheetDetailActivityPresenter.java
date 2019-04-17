package com.shichen.music.sport.presenter;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;

import com.shichen.music.basic.BasePresenter;
import com.shichen.music.data.CdlistBean;
import com.shichen.music.data.SheetSongListBean;
import com.shichen.music.data.SonglistBean;
import com.shichen.music.data.TagsBean;
import com.shichen.music.data.source.SheetDetailBeanRepository;
import com.shichen.music.data.source.remote.SheetDetailBeanRemoteSource;
import com.shichen.music.sport.SheetDetailActivity;
import com.shichen.music.sport.contract.SheetDetailContract;
import com.shichen.music.utils.PlayListUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SheetDetailActivityPresenter extends BasePresenter<SheetDetailContract.View> implements SheetDetailContract.Presenter {
    private long tid;
    PlayListUtil mPlayListUtil;

    @Override
    public void start(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        if (bundle.containsKey(SheetDetailActivity.SHEET_ID)) {
            tid = bundle.getLong(SheetDetailActivity.SHEET_ID);
            mPlayListUtil = PlayListUtil.getInstance(view.getContext());
        }
    }

    @Override
    public void refreshData() {
        view.unSubscribe();
        Disposable disposable =
                SheetDetailBeanRepository.getInstance(SheetDetailBeanRemoteSource.getInstance())
                        .getSheetDetail(tid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                sheetSongListBean -> bindData(sheetSongListBean, null, null),
                                throwable -> bindData(null, null, throwable));
        view.subscribe(disposable);
    }

    private void bindData(SheetSongListBean sheetSongListBean, String msg, Throwable throwable) {
        if (sheetSongListBean != null) {
            if (sheetSongListBean.getCdlist() != null) {
                if (sheetSongListBean.getCdlist().size() > 0) {
                    view.finishRefresh(true);
                    CdlistBean cdlistBean = sheetSongListBean.getCdlist().get(0);
                    view.setLogo(cdlistBean.getLogo());
                    view.setFrom(cdlistBean.getDissname());
                    view.setTitle(cdlistBean.getNickname());
                    view.setReplayCount("播放量:" + cdlistBean.getVisitnum());
                    if (cdlistBean.getTags() != null) {
                        if (cdlistBean.getTags().size() > 0) {
                            String tagStr = "";
                            for (TagsBean tag : cdlistBean.getTags()) {
                                tagStr = tagStr + " " + tag.getName();
                            }
                            view.setCollectCunt("标签:" + tagStr);
                        }
                    }
                    view.setSummary("简介");
                    view.setSummaryDetail(Html.fromHtml(cdlistBean.getDesc()));
                    view.setSongsCount("歌曲:" + cdlistBean.getSongnum() + "首");
                    view.setSongList(cdlistBean.getSonglist());
                } else {
                    view.finishRefresh(false);
                }
            } else {
                view.finishRefresh(false);
            }
        } else {
            if (TextUtils.isEmpty(msg)) {
                view.onFailed(null, throwable);
            } else {
                view.onFailed(msg, null);
            }
            view.finishRefresh(false);
        }
    }

    @Override
    public void playThisSong(SonglistBean songlistBean) {
        mPlayListUtil.clearList();
        mPlayListUtil.addToList(songlistBean.getSongmid());
        //进入播放页面
        view.gotoPlayActivity();
    }

    @Override
    public void addToPlayList(SonglistBean songlistBean) {
        mPlayListUtil.addToList(songlistBean.getSongmid());
        view.showGotoPlay();
    }

    @Override
    public void playAll(List<SonglistBean> songlistBeanList) {
        mPlayListUtil.clearList();
        List<String> newList = new ArrayList<>();
        for (SonglistBean bean : songlistBeanList) {
            newList.add(bean.getSongmid());
        }
        mPlayListUtil.addAllToList(newList);
        //进入播放页面
        view.gotoPlayActivity();
    }
}

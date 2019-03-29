package com.shichen.music.sport.presenter;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shichen.music.basic.BasePresenter;
import com.shichen.music.data.VPlaylistBean;
import com.shichen.music.data.source.CategoryBeanRepository;
import com.shichen.music.data.source.param.CategoryParam;
import com.shichen.music.data.source.remote.CategoryBeanRemoteSource;
import com.shichen.music.sport.contract.SportContract;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author shichen 754314442@qq.com
 * Created by Administrator on 2018/10/12.
 */
public class SportActivityPresenter extends BasePresenter<SportContract.View> implements SportContract.Presenter {
    private int curPage = 1;

    @Override
    public void start() {
        view.setIsRefresh();
    }

    @Override
    public void refreshList() {
        curPage = 1;
        CategoryParam param = new CategoryParam(new CategoryParam.Data(new CategoryParam.Data.PlaylistBean(new CategoryParam.Data.PlaylistBean.ParamBean(3317, curPage, 40, 3317))));
        view.unSubscribe();
        Disposable disposable =
                CategoryBeanRepository.getInstance(CategoryBeanRemoteSource.getInstance())
                        .getCategory(param)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                // onNext
                                tasks -> {
                                    refreshFinish(true, tasks.getPlaylist().getData().getV_playlist(), null);
                                },
                                // onError
                                throwable -> refreshFinish(false, null, throwable));
        view.subscribe(disposable);
    }

    private void refreshFinish(@NonNull boolean isSuccess, @Nullable List<VPlaylistBean> result, @Nullable Throwable throwable) {
        if (isSuccess) {
            if (result != null) {
                view.setCategory(result);
                view.refreshFinish(true);
            } else {
                view.onFailed("没有获取到相关数据", null);
                view.refreshFinish(false);
            }
        } else {
            view.onFailed(null, throwable);
            view.refreshFinish(false);
        }
    }

    @Override
    public void loadMoreList() {
        curPage++;
        CategoryParam param = new CategoryParam(new CategoryParam.Data(new CategoryParam.Data.PlaylistBean(new CategoryParam.Data.PlaylistBean.ParamBean(3317, curPage, 40, 3317))));
        view.unSubscribe();
        Disposable disposable =
                CategoryBeanRepository.getInstance(CategoryBeanRemoteSource.getInstance())
                        .getCategory(param)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                // onNext
                                tasks -> {
                                    loadMoreFinish(true, tasks.getPlaylist().getData().getV_playlist(), null);
                                },
                                // onError
                                throwable -> loadMoreFinish(false, null, throwable));
        view.subscribe(disposable);
    }

    private void loadMoreFinish(@NonNull boolean isSuccess, @Nullable List<VPlaylistBean> result, @Nullable Throwable throwable) {
        if (isSuccess) {
            if (result != null) {
                if (result.size() > 0) {
                    view.loadMoreFinish(result, true);
                } else {
                    view.loadMoreFinish(null, false);
                    curPage--;
                }
            } else {
                view.onFailed("没有获取到相关数据", null);
                view.loadMoreFinish(null, false);
                curPage--;
            }
        } else {
            view.onFailed(null, throwable);
            view.loadMoreFinish(null, false);
            curPage--;
        }
    }
}

package com.shichen.music.data.source;

import com.shichen.music.data.CategoryBean;
import com.shichen.music.data.VPlaylistBean;
import com.shichen.music.data.source.param.CategoryParam;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * 可以在其中添加缓存逻辑（数据库缓存）
 */
public class CategoryBeanRepository implements ICategoryBeanSource {

    private static CategoryBeanRepository INSTANCE;
    private ICategoryBeanSource mCategoryBeanRemoteSource;
    private ICategoryBeanSource mVPlayListBeanLocalSource;

    private CategoryBeanRepository(ICategoryBeanSource categoryBeanRemoteSource, ICategoryBeanSource mVPlayListBeanLocalSource) {
        this.mCategoryBeanRemoteSource = categoryBeanRemoteSource;
        this.mVPlayListBeanLocalSource = mVPlayListBeanLocalSource;
    }

    public static CategoryBeanRepository getInstance(ICategoryBeanSource categoryBeanRemoteSource, ICategoryBeanSource mVPlayListBeanLocalSource) {
        if (INSTANCE == null) {
            synchronized (CategoryBeanRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CategoryBeanRepository(categoryBeanRemoteSource, mVPlayListBeanLocalSource);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Flowable<List<VPlaylistBean>> getCategory(CategoryParam param) {
        Flowable<List<VPlaylistBean>> localSource = mVPlayListBeanLocalSource.getCategory(param);
        Flowable<List<VPlaylistBean>> remoteSource = getAndSaveList(param);
        return remoteSource.switchIfEmpty(localSource);
    }

    private Flowable<List<VPlaylistBean>> getAndSaveList(CategoryParam param) {
        return mCategoryBeanRemoteSource.getCategory(param).doOnNext(new Consumer<List<VPlaylistBean>>() {
            @Override
            public void accept(List<VPlaylistBean> list) throws Exception {
                if (list != null) {
                    if (list.size() > 0) {
                        clearList();
                        saveList(list);
                    }
                }
            }
        }).firstElement()
                .filter(list -> list.isEmpty()).toFlowable();
    }

    @Override
    public void saveList(List<VPlaylistBean> list) {
        mVPlayListBeanLocalSource.saveList(list);
    }

    @Override
    public void clearList() {
        mVPlayListBeanLocalSource.clearList();
    }
}

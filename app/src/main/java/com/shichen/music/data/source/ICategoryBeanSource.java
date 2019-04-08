package com.shichen.music.data.source;


import com.shichen.music.data.CategoryBean;
import com.shichen.music.data.VPlaylistBean;
import com.shichen.music.data.source.param.CategoryParam;

import java.util.List;

import io.reactivex.Flowable;

public interface ICategoryBeanSource {

    Flowable<List<VPlaylistBean>> getCategory(CategoryParam param);

    void saveList(List<VPlaylistBean> list);

    void clearList();
}

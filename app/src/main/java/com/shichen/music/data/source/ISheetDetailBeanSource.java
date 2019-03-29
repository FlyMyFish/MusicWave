package com.shichen.music.data.source;

import com.shichen.music.data.SheetSongListBean;

import io.reactivex.Flowable;

public interface ISheetDetailBeanSource {
    Flowable<SheetSongListBean> getSheetDetail(long tid);
}

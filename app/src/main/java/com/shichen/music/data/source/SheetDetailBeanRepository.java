package com.shichen.music.data.source;

import com.shichen.music.data.SheetSongListBean;
import com.shichen.music.data.source.remote.SheetDetailBeanRemoteSource;

import io.reactivex.Flowable;

public class SheetDetailBeanRepository implements ISheetDetailBeanSource {
    private static SheetDetailBeanRepository INSTANCE;

    private ISheetDetailBeanSource mSheetDetailBeanRemoteSource;

    private SheetDetailBeanRepository(SheetDetailBeanRemoteSource sheetDetailBeanRemoteSource) {
        mSheetDetailBeanRemoteSource = sheetDetailBeanRemoteSource;
    }

    public static SheetDetailBeanRepository getInstance(SheetDetailBeanRemoteSource sheetDetailBeanRemoteSource) {
        if (INSTANCE == null) {
            synchronized (SheetDetailBeanRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SheetDetailBeanRepository(sheetDetailBeanRemoteSource);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Flowable<SheetSongListBean> getSheetDetail(long tid) {
        return mSheetDetailBeanRemoteSource.getSheetDetail(tid);
    }
}

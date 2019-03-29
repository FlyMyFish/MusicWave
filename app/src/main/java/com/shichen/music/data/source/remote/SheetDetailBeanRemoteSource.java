package com.shichen.music.data.source.remote;

import com.shichen.music.data.SheetSongListBean;
import com.shichen.music.data.source.IDataApi;
import com.shichen.music.data.source.ISheetDetailBeanSource;
import com.shichen.music.utils.RetrofitHelper;

import io.reactivex.Flowable;

/**
 * @author shichen
 */
public class SheetDetailBeanRemoteSource implements ISheetDetailBeanSource {

    private static SheetDetailBeanRemoteSource INSTANCE;
    private IDataApi dataApi;

    private SheetDetailBeanRemoteSource(){
        dataApi= RetrofitHelper.getInstance().get().create(IDataApi.class);
    }

    public static SheetDetailBeanRemoteSource getInstance(){
        if (INSTANCE==null){
            synchronized (SheetDetailBeanRemoteSource.class){
                if (INSTANCE==null){
                    INSTANCE=new SheetDetailBeanRemoteSource();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Flowable<SheetSongListBean> getSheetDetail(long tid) {
        return dataApi.getSheetDetail(tid,"https://y.qq.com/n/yqq/playsquare/3838219942.html");
    }
}

package com.shichen.music.data.source.remote;

import android.util.Log;

import com.shichen.music.data.SheetSongListBean;
import com.shichen.music.data.source.IDataApi;
import com.shichen.music.utils.RetrofitHelper;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * @author by Administrator, Email xx@xx.com, Date on 2019/3/28
 */
public class SheetDetailBeanRemoteSourceTest {
    IDataApi dataApi;

    @Before
    public void init() {
        initRxJava2();
        dataApi = RetrofitHelper.getInstance().get().create(IDataApi.class);
    }

    private void initRxJava2() {
        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
        RxAndroidPlugins.reset();
        RxAndroidPlugins.setMainThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
    }

    @Test
    public void getDetail() {
        long tid=3838219942L;

        dataApi.getSheetDetail(tid,"https://y.qq.com/n/yqq/playsquare/3838219942.html")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sheetSongListBean -> parse(sheetSongListBean));
    }

    private void parse(SheetSongListBean body) {
        try {
            Log.e("getDetail", body.toString());
        } catch (Exception e) {

        }
    }
}

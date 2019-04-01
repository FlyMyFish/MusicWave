package com.shichen.music.data.source.remote;

import android.util.Log;

import com.google.common.base.Optional;
import com.shichen.music.data.SongTokenBean;
import com.shichen.music.data.SongTokenItemBean;
import com.shichen.music.data.source.IDataApi;
import com.shichen.music.data.source.ISongTokenSource;
import com.shichen.music.utils.RetrofitHelper;

import org.reactivestreams.Publisher;

import java.util.Calendar;
import java.util.Date;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/4/1
 */
public class SongTokenItemBeanRemoteSource implements ISongTokenSource {
    private static SongTokenItemBeanRemoteSource INSTANCE;
    private IDataApi dataApi;

    private SongTokenItemBeanRemoteSource() {
        dataApi = RetrofitHelper.getInstance().get().create(IDataApi.class);
    }

    public static SongTokenItemBeanRemoteSource getInstance() {
        if (INSTANCE == null) {
            synchronized (SongTokenItemBeanRemoteSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SongTokenItemBeanRemoteSource();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void save(SongTokenItemBean songTokenItemBean) {

    }

    @Override
    public Flowable<Optional<SongTokenItemBean>> getTokenByMid(String mid) {
        return dataApi.getSongToken(mid, "C400" + mid + ".ma4").map(new Function<SongTokenBean, Optional<SongTokenItemBean>>() {
            @Override
            public Optional<SongTokenItemBean> apply(SongTokenBean songTokenBean) throws Exception {
                if (songTokenBean.getData() != null) {
                    int expiration = songTokenBean.getData().getExpiration();
                    Date now = Calendar.getInstance().getTime();
                    if (songTokenBean.getData().getItems() != null) {
                        if (songTokenBean.getData().getItems().size() > 0) {
                            SongTokenItemBean songTokenItemBean = songTokenBean.getData().getItems().get(0);
                            songTokenItemBean.setExpiration(expiration);
                            songTokenItemBean.setUpdateTime(now.getTime());
                            Log.e("SongTokenItemRemote", songTokenItemBean.toString());
                            return Optional.of(songTokenItemBean);
                        } else {
                            Log.e("SongTokenItemRemote", "null");
                            return null;
                        }
                    } else {
                        Log.e("SongTokenItemRemote", "null");
                        return null;
                    }
                } else {
                    Log.e("SongTokenItemRemote", "null");
                    return null;
                }
            }
        });
    }
}

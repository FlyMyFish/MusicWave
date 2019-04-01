package com.shichen.music.data.source;

import android.util.Log;

import com.google.common.base.Optional;
import com.shichen.music.data.SongTokenItemBean;
import com.shichen.music.utils.LogUtils;

import org.reactivestreams.Publisher;

import java.util.Calendar;
import java.util.Date;

import io.reactivex.Flowable;
import io.reactivex.SingleSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/4/1
 */
public class SongTokenItemBeanRepository implements ISongTokenSource {
    private static SongTokenItemBeanRepository INSTANCE;
    private ISongTokenSource mSongTokenItemLocalSource;
    private ISongTokenSource mSongTokenItemRemoteSource;

    private SongTokenItemBeanRepository(ISongTokenSource mSongTokenItemLocalSource, ISongTokenSource mSongTokenItemRemoteSource) {
        this.mSongTokenItemLocalSource = mSongTokenItemLocalSource;
        this.mSongTokenItemRemoteSource = mSongTokenItemRemoteSource;
    }

    public static SongTokenItemBeanRepository getInstance(ISongTokenSource mSongTokenItemLocalSource, ISongTokenSource mSongTokenItemRemoteSource) {
        if (INSTANCE == null) {
            synchronized (SongTokenItemBeanRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SongTokenItemBeanRepository(mSongTokenItemLocalSource, mSongTokenItemRemoteSource);
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
        //当前时间
        Date date = Calendar.getInstance().getTime();
        //从数据库读数据
        Flowable<Optional<SongTokenItemBean>> localToken = getLocal(mid, date);
        //从网络读数据
        Flowable<Optional<SongTokenItemBean>> remoteToken = getAndSaveRemote(mid, date);
        //如果没有从数据库读到符合筛选条件的数据则从网络读数据
        return localToken.switchIfEmpty(remoteToken);
    }

    private Flowable<Optional<SongTokenItemBean>> getLocal(String mid, Date date) {
        return mSongTokenItemLocalSource.getTokenByMid(mid)
                .firstElement()
                .filter(new Predicate<Optional<SongTokenItemBean>>() {
                    @Override
                    public boolean test(Optional<SongTokenItemBean> songTokenItemBeanOptional) throws Exception {
                        LogUtils.Log("SongTokenItemRepository", "date = > " + date.getTime());
                        if (songTokenItemBeanOptional.isPresent()) {
                            SongTokenItemBean songTokenItemBean = songTokenItemBeanOptional.get();
                            //从数据库读数据时的筛选条件getExpiration()有效时间单位是秒，乘以1000大于现在的时间减去上一次更新数据的时间时表示该数据信息过期了，需要重新获取
                            if (songTokenItemBean.getExpiration() * 1000 > (date.getTime() - songTokenItemBean.getUpdateTime())) {

                                LogUtils.Log("SongTokenItemRepository", "test = > true");
                                return true;
                            }
                            LogUtils.Log("SongTokenItemRepository", "test = > false");
                            return false;
                        } else {
                            LogUtils.Log("SongTokenItemRepository", "test = > false");
                            return false;
                        }
                    }
                }).toFlowable();
    }

    /**
     * 网络请求并将数据存入本地数据库
     *
     * @param mid
     * @param date 数据操作时间
     * @return
     */
    private Flowable<Optional<SongTokenItemBean>> getAndSaveRemote(String mid, Date date) {
        return mSongTokenItemRemoteSource.getTokenByMid(mid)
                .doOnNext(new Consumer<Optional<SongTokenItemBean>>() {
                    @Override
                    public void accept(Optional<SongTokenItemBean> songTokenItemBeanOptional) throws Exception {
                        if (songTokenItemBeanOptional.isPresent()) {
                            saveRemote(date, songTokenItemBeanOptional.get());
                        }
                    }
                });
    }

    /**
     * 更新数据操作时间之后存入本地数据库
     *
     * @param date
     * @param songTokenItemBean
     */
    private void saveRemote(Date date, SongTokenItemBean songTokenItemBean) {
        songTokenItemBean.setUpdateTime(date.getTime());
        mSongTokenItemLocalSource.save(songTokenItemBean);
    }
}

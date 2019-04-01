package com.shichen.music.data.source.local;

import com.google.common.base.Optional;
import com.shichen.music.data.SongTokenItemBean;
import com.shichen.music.data.source.ISongTokenSource;


import io.reactivex.Flowable;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/4/1
 */
public class SongTokenItemBeanLocalSource implements ISongTokenSource {
    private static SongTokenItemBeanLocalSource INSTANCE;

    private ISongTokenItemDao songTokenItemDao;

    private SongTokenItemBeanLocalSource(ISongTokenItemDao songTokenItemDao) {
        this.songTokenItemDao = songTokenItemDao;
    }

    public static SongTokenItemBeanLocalSource getInstance(ISongTokenItemDao songTokenItemDao) {
        if (INSTANCE == null) {
            synchronized (SongTokenItemBeanLocalSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SongTokenItemBeanLocalSource(songTokenItemDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void save(SongTokenItemBean songTokenItemBean) {
        songTokenItemDao.save(songTokenItemBean);
    }

    @Override
    public Flowable<Optional<SongTokenItemBean>> getTokenByMid(String mid) {
        return songTokenItemDao.getSongTokenItemByMid(mid);
    }
}

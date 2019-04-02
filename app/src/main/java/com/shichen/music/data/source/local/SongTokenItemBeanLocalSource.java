package com.shichen.music.data.source.local;

import android.text.TextUtils;

import com.google.common.base.Optional;
import com.shichen.music.data.SongTokenItemBean;
import com.shichen.music.data.source.ISongTokenSource;


import io.reactivex.Flowable;
import io.reactivex.functions.Function;

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

    @Override
    public Flowable<Optional<String>> getLyrics(String mid) {
        return songTokenItemDao.getSongTokenItemByMid(mid)
                .map(new Function<Optional<SongTokenItemBean>, Optional<String>>() {
                    @Override
                    public Optional<String> apply(Optional<SongTokenItemBean> songTokenItemBeanOptional) throws Exception {
                        if (songTokenItemBeanOptional.isPresent()) {
                            SongTokenItemBean songTokenItemBean = songTokenItemBeanOptional.get();
                            if (!TextUtils.isEmpty(songTokenItemBean.getLyrics())) {
                                return Optional.of(songTokenItemBean.getLyrics());
                            } else {
                                return Optional.of("");
                            }
                        } else {
                            return Optional.of("");
                        }
                    }
                });
    }

    @Override
    public void updateLyrics(String mid, String lyrics) {
        songTokenItemDao.updateLyrics(mid, lyrics);
    }
}

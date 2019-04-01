package com.shichen.music.data.source;

import com.google.common.base.Optional;
import com.shichen.music.data.SongTokenItemBean;


import io.reactivex.Flowable;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/4/1
 */
public interface ISongTokenSource {
    Flowable<Optional<SongTokenItemBean>> getTokenByMid(String mid);
    void save(SongTokenItemBean songTokenItemBean);
}

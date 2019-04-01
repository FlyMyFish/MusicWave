package com.shichen.music.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.google.common.base.Optional;
import com.shichen.music.data.SongTokenItemBean;


import io.reactivex.Flowable;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/4/1
 */
@Dao
public interface ISongTokenItemDao {
    @Query("SELECT * FROM song_token_item WHERE songmid = :mid")
    Flowable<Optional<SongTokenItemBean>> getSongTokenItemByMid(String mid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(SongTokenItemBean songTokenItemBean);
}

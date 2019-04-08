package com.shichen.music.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.shichen.music.data.VPlaylistBean;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/4/8
 */
@Dao
public interface IVPlayListDao {
    @Query("SELECT * FROM v_play_list")
    Flowable<List<VPlaylistBean>> getList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveList(List<VPlaylistBean> list);

    @Query("DELETE FROM v_play_list")
    void clearList();
}

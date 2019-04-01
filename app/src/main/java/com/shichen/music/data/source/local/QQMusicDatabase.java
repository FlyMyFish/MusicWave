package com.shichen.music.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.shichen.music.data.SongTokenItemBean;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/4/1
 */
@Database(entities = {SongTokenItemBean.class}, version = 1,exportSchema = false)
public abstract class QQMusicDatabase extends RoomDatabase {
    private static QQMusicDatabase INSTANCE;
    private static final Object sLock = new Object();

    public abstract ISongTokenItemDao songTokenItemDao();

    public static QQMusicDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        QQMusicDatabase.class, "QQMusic.db")
                        .build();
            }
            return INSTANCE;
        }
    }
}

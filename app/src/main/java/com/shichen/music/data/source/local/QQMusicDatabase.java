package com.shichen.music.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.shichen.music.data.ListTypeConverter;
import com.shichen.music.data.SongTokenItemBean;
import com.shichen.music.data.VPlaylistBean;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/4/1
 */
@Database(entities = {SongTokenItemBean.class, VPlaylistBean.class}, version = 1,exportSchema = false)
@TypeConverters(ListTypeConverter.class)
public abstract class QQMusicDatabase extends RoomDatabase {
    private static QQMusicDatabase INSTANCE;
    private static final Object sLock = new Object();

    public abstract ISongTokenItemDao songTokenItemDao();

    public abstract IVPlayListDao ivPlayListDao();

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

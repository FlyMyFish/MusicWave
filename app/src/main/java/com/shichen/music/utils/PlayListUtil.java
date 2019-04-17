package com.shichen.music.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shichen 754314442@qq.com
 * Created by shichen on 2019/4/16.
 */
public class PlayListUtil {
    private static PlayListUtil INSTANCE;
    private List<String> songMidList;
    private final String MUSIC_PLAY_LIST = "music_play_list";
    private SharedPreferences sharedPreferences;

    private PlayListUtil(Context context) {
        songMidList = new ArrayList<>();
        sharedPreferences = context.getSharedPreferences(MUSIC_PLAY_LIST, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            List<String> localList = GsonUtils.getInstance().get().fromJson(sharedPreferences.getString(MUSIC_PLAY_LIST, ""), new TypeToken<List<String>>() {
            }.getType());
            if (localList != null) {
                songMidList.addAll(localList);
            } else {
                songMidList = new ArrayList<>();
            }
        } else {
            songMidList = new ArrayList<>();
        }
    }

    public static PlayListUtil getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (PlayListUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PlayListUtil(context);
                }
            }
        }
        return INSTANCE;
    }

    public void clearList() {
        songMidList.clear();
        saveList();
    }

    public void addToList(String songMid) {
        boolean have = false;
        for (String songMidItem : songMidList) {
            if (songMidItem.equals(songMid)) {
                have = true;
            }
        }
        if (!have) {
            songMidList.add(songMid);
        }
        saveList();
    }

    public void addAllToList(List<String> wantToSave) {
        songMidList.addAll(wantToSave);
        saveList();
    }

    public List<String> getPlayList() {
        return songMidList;
    }

    private void saveList() {
        String playListStr = GsonUtils.getInstance().get().toJson(songMidList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MUSIC_PLAY_LIST, playListStr);
        editor.apply();
    }
}

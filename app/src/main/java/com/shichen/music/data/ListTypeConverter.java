package com.shichen.music.data;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.reflect.TypeToken;
import com.shichen.music.utils.GsonUtils;

import java.util.List;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/4/8
 */
public class ListTypeConverter {

    @TypeConverter
    public static List<Integer> revertList(String listJson) {
        return GsonUtils.getInstance().get().fromJson(listJson, new TypeToken<List<Integer>>() {
        }.getType());
    }

    @TypeConverter
    public static String convertList(List<Integer> list) {
        return GsonUtils.getInstance().get().toJson(list);
    }
}

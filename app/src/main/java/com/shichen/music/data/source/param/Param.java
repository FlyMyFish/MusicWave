package com.shichen.music.data.source.param;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.shichen.music.utils.GsonUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

public class Param {
    @Override
    public String toString() {
        return GsonUtils.getInstance().get().toJson(this);
    }

    public Map<String, Object> makeMap() {
        JsonParser parser=new JsonParser();
        JsonElement element=parser.parse(toString());
        JsonObject object=element.getAsJsonObject();
        Set<Map.Entry<String,JsonElement>> entrySet=object.entrySet();
        Iterator<Map.Entry<String,JsonElement>> iterable=entrySet.iterator();
        Map<String,Object> makeMap=new HashMap<>();
        while (iterable.hasNext()){
            Map.Entry<String,JsonElement> entry=iterable.next();
            String key=entry.getKey();
            makeMap.put(key,entry.getValue());
        }
        return makeMap;
    }
}

package com.shichen.music.service;

import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;

import java.util.Iterator;
import java.util.List;

/**
 * @author shichen 754314442@qq.com
 * Created by shichen on 2019/7/9.
 */
public class AbstractMusicSource implements MusicSource {
    @Override
    public void load() {

    }

    @Override
    public boolean whenReady(Boolean performAction) {
        return false;
    }

    @Override
    public List<MediaMetadataCompat> search(String query, Bundle extras) {
        return null;
    }

    @Override
    public Iterator<MediaMetadataCompat> iterator() {
        return null;
    }
}

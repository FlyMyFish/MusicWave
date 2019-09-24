package com.shichen.music.service;

import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;

import java.util.List;

/**
 * @author shichen 754314442@qq.com
 * Created by shichen on 2019/7/9.
 */
public interface MusicSource extends Iterable<MediaMetadataCompat>{
    void load();

    boolean whenReady(Boolean performAction);

    List<MediaMetadataCompat> search(String query, Bundle extras );
}



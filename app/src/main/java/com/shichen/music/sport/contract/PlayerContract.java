package com.shichen.music.sport.contract;

import android.os.Bundle;

import com.google.android.exoplayer2.source.MediaSource;
import com.shichen.music.basic.BaseContract;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/3/29
 */
public interface PlayerContract {
    interface View extends BaseContract.View{
        void initPlayer();
        void setLyrics(String lyrics);
        void setMediaSource(MediaSource mediaSource);
        void tryNext();
        int getCurrentPeriodIndex();
    }
    interface Presenter extends BaseContract.Presenter<View>{
        void setBundle(Bundle bundle);
        void getLyrics();
        void setCurrentPeriod(int currentPeriod);
    }
}

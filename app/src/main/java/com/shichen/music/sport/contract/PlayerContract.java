package com.shichen.music.sport.contract;

import android.os.Bundle;

import com.shichen.music.basic.BaseContract;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/3/29
 */
public interface PlayerContract {
    interface View extends BaseContract.View{
        void initPlayer(String fileName,String vkey);
        void setLyrics(String lyrics);
    }
    interface Presenter extends BaseContract.Presenter<View>{
        void setBundle(Bundle bundle);
    }
}

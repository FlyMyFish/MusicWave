package com.shichen.music.sport.contract;

import android.os.Bundle;
import android.text.Spanned;

import com.shichen.music.basic.BaseContract;
import com.shichen.music.data.SheetSongListBean;
import com.shichen.music.data.SonglistBean;

import java.util.List;

public interface SheetDetailContract {
    interface View extends BaseContract.View{
        void setData(SheetSongListBean sheetDetail);
        void setLogo(String url);
        void setFrom(String from);
        void setTitle(String title);
        void setCollectCunt(String collectCunt);
        void setReplayCount(String replayCount);
        void setSummary(String summary);
        void setSummaryDetail(Spanned summaryDetail);
        void setSongsCount(String songsCount);
        void setAutoRefresh();
        void finishRefresh(boolean ifSuccess);
        void setSongList(List<SonglistBean> songList);
    }
    interface Presenter extends BaseContract.Presenter<View>{
        void start(Bundle bundle);
        void refreshData();
    }
}

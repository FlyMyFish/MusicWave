package com.shichen.music.sport.contract;


import com.shichen.music.basic.BaseContract;
import com.shichen.music.data.VPlaylistBean;

import java.util.List;

/**
 * @author shichen 754314442@qq.com
 * Created by Administrator on 2018/10/12.
 */
public interface SportContract {
    interface View extends BaseContract.View{

        void setIsRefresh();

        void refreshFinish(boolean ifSuccess);

        void setCategory(List<VPlaylistBean> vPlaylistBeanList);

        void loadMoreFinish(List<VPlaylistBean> vPlaylistBeanList,boolean ifSuccess);

    }

    interface Presenter extends BaseContract.Presenter<View>{
        void start();
        void refreshList();
        void loadMoreList();
    }
}

package com.shichen.music.data.source.local;

import com.shichen.music.data.VPlaylistBean;
import com.shichen.music.data.source.ICategoryBeanSource;
import com.shichen.music.data.source.param.CategoryParam;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/4/8
 */
public class VPlayListBeanLocalSource implements ICategoryBeanSource {

    private static VPlayListBeanLocalSource INSTANCE;

    private IVPlayListDao mIVPlayListDao;

    private VPlayListBeanLocalSource(IVPlayListDao mIVPlayListDao){
        this.mIVPlayListDao=mIVPlayListDao;
    }

    public static VPlayListBeanLocalSource getInstance(IVPlayListDao mIVPlayListDao){
        if (INSTANCE==null){
            synchronized (VPlayListBeanLocalSource.class){
                if (INSTANCE==null){
                    INSTANCE=new VPlayListBeanLocalSource(mIVPlayListDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Flowable<List<VPlaylistBean>> getCategory(CategoryParam param) {
        return mIVPlayListDao.getList();
    }

    @Override
    public void saveList(List<VPlaylistBean> list) {
        mIVPlayListDao.saveList(list);
    }

    @Override
    public void clearList() {
        mIVPlayListDao.clearList();
    }
}

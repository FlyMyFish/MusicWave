package com.shichen.music.data.source.remote;

import com.shichen.music.data.CategoryBean;
import com.shichen.music.data.VPlaylistBean;
import com.shichen.music.data.source.ICategoryBeanSource;
import com.shichen.music.data.source.IDataApi;
import com.shichen.music.data.source.param.CategoryParam;
import com.shichen.music.utils.RetrofitHelper;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

public class CategoryBeanRemoteSource implements ICategoryBeanSource {

    private final String TAG = "CategoryRemoteSource";

    private static CategoryBeanRemoteSource INSTANCE;

    private IDataApi dataApi;

    private CategoryBeanRemoteSource() {
        dataApi = RetrofitHelper.getInstance().get().create(IDataApi.class);
    }

    public static CategoryBeanRemoteSource getInstance() {
        if (INSTANCE == null) {
            synchronized (CategoryBeanRemoteSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CategoryBeanRemoteSource();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Flowable<List<VPlaylistBean>> getCategory(CategoryParam param) {
        return dataApi.getCategory(param.makeMap()).map(new Function<CategoryBean, List<VPlaylistBean>>() {
            @Override
            public List<VPlaylistBean> apply(CategoryBean categoryBean) throws Exception {
                if (categoryBean != null) {
                    if (categoryBean.getPlaylist() != null) {
                        if (categoryBean.getPlaylist().getData() != null) {
                            if (categoryBean.getPlaylist().getData().getV_playlist() != null) {
                                return categoryBean.getPlaylist().getData().getV_playlist();
                            }
                        }
                    }
                }
                return null;
            }
        });
    }

    @Override
    public void saveList(List<VPlaylistBean> list) {

    }

    @Override
    public void clearList() {

    }
}

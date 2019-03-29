package com.shichen.music.data.source.remote;

import com.shichen.music.data.CategoryBean;
import com.shichen.music.data.source.ICategoryBeanSource;
import com.shichen.music.data.source.IDataApi;
import com.shichen.music.data.source.param.CategoryParam;
import com.shichen.music.utils.RetrofitHelper;

import io.reactivex.Flowable;

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
    public Flowable<CategoryBean> getCategory(CategoryParam param) {
        return dataApi.getCategory(param.makeMap());
    }
}

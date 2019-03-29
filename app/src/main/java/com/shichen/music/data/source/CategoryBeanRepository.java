package com.shichen.music.data.source;

import com.shichen.music.data.CategoryBean;
import com.shichen.music.data.source.param.CategoryParam;

import io.reactivex.Flowable;

/**
 * 可以在其中添加缓存逻辑（数据库缓存）
 */
public class CategoryBeanRepository implements ICategoryBeanSource {

    private static CategoryBeanRepository INSTANCE;
    private ICategoryBeanSource mCategoryBeanRemoteSource;

    private CategoryBeanRepository(ICategoryBeanSource categoryBeanRemoteSource) {
        this.mCategoryBeanRemoteSource = categoryBeanRemoteSource;
    }

    public static CategoryBeanRepository getInstance(ICategoryBeanSource categoryBeanRemoteSource) {
        if (INSTANCE == null) {
            synchronized (CategoryBeanRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CategoryBeanRepository(categoryBeanRemoteSource);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Flowable<CategoryBean> getCategory(CategoryParam param) {
        return mCategoryBeanRemoteSource.getCategory(param);
    }
}

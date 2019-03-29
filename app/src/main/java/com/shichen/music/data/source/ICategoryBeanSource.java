package com.shichen.music.data.source;


import com.shichen.music.data.CategoryBean;
import com.shichen.music.data.source.param.CategoryParam;

import io.reactivex.Flowable;

public interface ICategoryBeanSource {

    Flowable<CategoryBean> getCategory(CategoryParam param);
}

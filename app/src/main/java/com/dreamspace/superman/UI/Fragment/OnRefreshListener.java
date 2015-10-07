package com.dreamspace.superman.UI.Fragment;

import com.dreamspace.superman.model.api.LessonInfo;

import java.util.List;

/**
 * Created by Wells on 2015/10/5.
 */
public interface OnRefreshListener<T> {
    void onFinish(List<T> mEntities);

    void onError();
}


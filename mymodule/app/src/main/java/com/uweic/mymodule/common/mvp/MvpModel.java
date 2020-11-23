package com.uweic.mymodule.common.mvp;

/**
 * Created by haoxuhong on 2019/12/23.
 *
 * @description: MVP 模型基类
 */
public abstract class MvpModel<L> {

    private L mListener;

    public void setListener(L listener) {
        mListener = listener;
    }

    public L getListener() {
        return mListener;
    }
}
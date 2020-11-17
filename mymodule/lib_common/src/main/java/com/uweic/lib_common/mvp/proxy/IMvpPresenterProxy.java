package com.uweic.lib_common.mvp.proxy;

/**
 * Created by haoxuhong on 2019/12/23.
 *
 * @description:  逻辑层代理接口
 */
public interface IMvpPresenterProxy {
    /**
     * 绑定 Presenter
     */
    void bindPresenter();

    /**
     * 解绑 Presenter
     */
    void unbindPresenter();
}
package com.uweic.mymodule.common.mvp.proxy;

/**
 * Created by haoxuhong on 2019/12/23.
 *
 * @description:  模型层代理接口
 */
public interface IMvpModelProxy {
    /**
     * 绑定 Model
     */
    void bindModel();

    /**
     * 解绑 Model
     */
    void unbindModel();
}
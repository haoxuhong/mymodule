package com.uweic.mymodule.common.mvp;

import android.content.Context;
import android.view.View;

/**
 * Created by haoxuhong on 2019/12/23.
 *
 * @description: MVP 通用性接口
 */
public interface IMvpView {

    /*** 获取上下文对象*/
    Context getContext();
// 请求类型: 加载中  网络异常    没有数据  token失效   加载完成   数据错误

    /*** 加载中*/
    void onLoading(View view);

    /***加载完成*/
    void onComplete();

    /***用于请求的数据为空的状态*/
    void onEmpty(boolean isShowLayoutHint, String hintStr);

    /*** 用于请求数据出错*/
    void onError(boolean isShowLayoutHint, String hintStr, boolean isNetworkAvailable);

    /***token失效*/
    void onTokenFailure();
}
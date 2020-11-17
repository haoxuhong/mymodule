package com.uweic.lib_common.mvp;

import android.content.Context;
import android.text.TextUtils;

import androidx.databinding.ViewDataBinding;

import com.uweic.lib_base.base.BaseActivity;
import com.uweic.lib_common.R;
import com.uweic.lib_common.mvp.proxy.IMvpPresenterProxy;
import com.uweic.lib_common.mvp.proxy.MvpPresenterProxyImpl;


/**
 * Created by haoxuhong on 2019/12/23.
 *
 * @description: MVP Activity 基类
 */
public abstract class MvpActivity<V extends ViewDataBinding> extends BaseActivity<V> implements IMvpView {

    private IMvpPresenterProxy mMvpProxy;

    @Override
    public void initActivity() {
        mMvpProxy = createPresenterProxy();
        mMvpProxy.bindPresenter();
        super.initActivity();
    }

    protected IMvpPresenterProxy createPresenterProxy() {
        return new MvpPresenterProxyImpl(this);
    }

    @Override
    protected void onDestroy() {
        mMvpProxy.unbindPresenter();
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onLoading() {
        showLoading();
    }

    @Override
    public void onComplete() {
        showComplete();

    }

    @Override
    public void onEmpty(boolean isShowLayoutHint, String hintStr) {
        if (isShowLayoutHint) {
            showEmpty();
        }
        if (!TextUtils.isEmpty(hintStr)) {
            toast(hintStr);
        }
        showComplete();
    }

    /**网络错误   其他错误
     * @param isShowLayoutHint
     * @param hintStr
     * @param isNetworkAvailable
     */
    @Override
    public void onError(boolean isShowLayoutHint, String hintStr, boolean isNetworkAvailable) {
        if (isShowLayoutHint) {
            showError(isNetworkAvailable);
        }
        if (isNetworkAvailable && !TextUtils.isEmpty(hintStr)) {
            toast(hintStr);
        } else {
            toast(getString(R.string.common_internet_error));
        }
        showComplete();
    }

    @Override
    public void onTokenFailure() {
        showComplete();
        // TODO: 2020/8/26  添加登录模块后
//        startActivity(LoginActivity.class);
    }
}
package com.uweic.lib_common.mvp;

import android.text.TextUtils;

import androidx.databinding.ViewDataBinding;

import com.uweic.lib_base.base.BaseFragment;
import com.uweic.lib_common.R;
import com.uweic.lib_common.mvp.proxy.IMvpPresenterProxy;
import com.uweic.lib_common.mvp.proxy.MvpPresenterProxyImpl;


/**
 * Created by haoxuhong on 2020/1/2.
 *
 * @description: MVP Fragment 基类
 */

public abstract class MvpFragment<A extends MvpActivity,V extends ViewDataBinding> extends BaseFragment<A,V> implements IMvpView {
    private IMvpPresenterProxy mMvpProxy;

    @Override
    protected void initFragment() {
        mMvpProxy = createPresenterProxy();
        mMvpProxy.bindPresenter();
        super.initFragment();
    }

    protected IMvpPresenterProxy createPresenterProxy() {
        return new MvpPresenterProxyImpl(this);
    }

    @Override
    public void onDestroy() {
        if (mMvpProxy != null) {
            mMvpProxy.unbindPresenter();
        }
        super.onDestroy();
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

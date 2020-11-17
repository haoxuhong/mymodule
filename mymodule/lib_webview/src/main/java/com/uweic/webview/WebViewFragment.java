package com.uweic.webview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.uweic.lib_base.loadsir.EmptyCallback;
import com.uweic.lib_base.loadsir.ErrorCallback;
import com.uweic.lib_base.loadsir.LoadingCallback;
import com.uweic.lib_base.loadsir.TimeoutCallback;
import com.uweic.webview.databinding.FragmentWebviewBinding;
import com.uweic.webview.utils.Constants;

public class WebViewFragment extends Fragment implements WebViewCallBack, OnRefreshListener {
    private String mUrl;
    private FragmentWebviewBinding mBinding;
    private LoadService mLoadService;
    private boolean mCanNativeRefresh;
    private boolean mIsError = false;
    private static final String TAG = "WebViewFragment";

    public static WebViewFragment newInstance(String url, boolean canNativeRefresh) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.URL, url);
        bundle.putBoolean(Constants.CAN_NATIVE_REFRESH, canNativeRefresh);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mUrl = bundle.getString(Constants.URL);
            mCanNativeRefresh = bundle.getBoolean(Constants.CAN_NATIVE_REFRESH);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_webview, container, false);
        mBinding.webview.registerWebViewCallBack(this);
        mBinding.webview.loadUrl(mUrl);

        initLoadSir(mBinding.smartrefreshlayout, "");
        if (mLoadService != null) {
            mLoadService.showSuccess();
        }

        mBinding.smartrefreshlayout.setOnRefreshListener(this);
        mBinding.smartrefreshlayout.setEnableRefresh(mCanNativeRefresh);
        mBinding.smartrefreshlayout.setEnableLoadMore(false);
        return mLoadService.getLoadLayout();
    }
    //TODO  如何实现动态修改提示文字
    private void initLoadSir(View content, String title) {
        if (content != null) {
            if (mLoadService != null) {
                mLoadService.showSuccess();
                mLoadService = null;
            }
            LoadingCallback.Builder loadingBuilder = new LoadingCallback.Builder();
            loadingBuilder.setTitle(title);
            LoadingCallback build = loadingBuilder.build();
            LoadSir loadSir = new LoadSir.Builder()
                    .addCallback(build)
                    .addCallback(new EmptyCallback())
                    .addCallback(new ErrorCallback())
                    .addCallback(new TimeoutCallback())
                    .build();
            mLoadService = loadSir.register(content, new Callback.OnReloadListener() {
                @Override
                public void onReload(View v) {
                    mLoadService.showCallback(LoadingCallback.class);
                    mBinding.webview.reload();
                }
            });
        }
    }
    @Override
    public void pageStarted(String url) {
        initLoadSir(mBinding.smartrefreshlayout, this.getString(com.uweic.lib_base.R.string.common_loading));
        if (mLoadService != null) {
            mLoadService.showCallback(LoadingCallback.class);
        }
    }

    @Override
    public void pageFinished(String url) {
        if (mIsError) {
            mBinding.smartrefreshlayout.setEnableRefresh(true);
        } else {
            mBinding.smartrefreshlayout.setEnableRefresh(mCanNativeRefresh);
        }
        Log.d(TAG, "pageFinished");
        mBinding.smartrefreshlayout.finishRefresh();
        if (mLoadService != null) {
            if (mIsError) {
                mLoadService.showCallback(ErrorCallback.class);
            } else {
                mLoadService.showSuccess();
            }
        }
        mIsError = false;
    }

    @Override
    public void onError() {
        Log.e(TAG, "onError");
        mIsError = true;
        mBinding.smartrefreshlayout.finishRefresh();
    }

    @Override
    public void updateTitle(String title) {
        if (getActivity() instanceof WebViewActivity) {
            ((WebViewActivity) getActivity()).updateTitle(title);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mBinding.webview.reload();
    }
}

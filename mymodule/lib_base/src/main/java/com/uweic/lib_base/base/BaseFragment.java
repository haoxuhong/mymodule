package com.uweic.lib_base.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.hjq.toast.ToastUtils;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.uweic.lib_base.R;
import com.uweic.lib_base.loadsir.EmptyCallback;
import com.uweic.lib_base.loadsir.ErrorCallback;
import com.uweic.lib_base.loadsir.LoadingCallback;
import com.uweic.lib_base.loadsir.TimeoutCallback;
import com.uweic.lib_base.utils.MyLogManager;
import com.uweic.lib_dialog.action.ActivityAction;
import com.uweic.lib_dialog.action.BundleAction;
import com.uweic.lib_dialog.action.ClickAction;
import com.uweic.lib_dialog.action.HandlerAction;

import java.io.Serializable;

/**
 * Created by haoxuhong on 2019/12/23.
 *
 * @description: Fragment 懒加载基类
 */

public abstract class BaseFragment<A extends BaseActivity, V extends ViewDataBinding> extends Fragment
        implements ActivityAction, ClickAction, HandlerAction, BundleAction {
    public String mTag = this.getClass().getSimpleName();
    protected LoadService mBaseLoadService;

    private A mActivity;//Activity对象

    //    private View mRootView;//根布局
    protected V mBinding;
    protected View contentView;

    private boolean isLazyLoad;//是否进行过懒加载

    private boolean isFragmentVisible;//Fragment 是否可见

    private boolean isReplaceFragment;//是否是 replace Fragment 的形式


    protected abstract int onCreateFragmentViewId();//引入布局

    protected abstract void initView();//初始化控件

    protected void onReload(View v) {
    }//重新加载

    @Override
    public View getView() {
        return mBinding.getRoot();
    }

    /**
     * 根据资源 id 获取一个 View 对象
     */
    @Override
    public <V extends View> V findViewById(@IdRes int id) {
        return getView().findViewById(id);
    }

    @Override
    public Bundle getBundle() {
        return getArguments();
    }

    /**
     * 获取绑定的 Activity，防止出现 getActivity 为空
     */
    public A getAttachActivity() {
        return mActivity;
    }

    /**
     * 获得全局的 Activity
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (A) requireActivity();
    }

    /**
     * replace Fragment时使用，ViewPager 切换时会回调此方法
     * setUserVisibleHint是在onCreateView之前调用的
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyLogManager.d(mTag, "setUserVisibleHint   " + "isVisibleToUser: " + isVisibleToUser + " mRootView: " + mBinding.getRoot() + " isFragmentVisible: " + isFragmentVisible + " isLazyLoad: " + isLazyLoad);
        this.isReplaceFragment = true;
        this.isFragmentVisible = isVisibleToUser;
        if (isVisibleToUser && mBinding.getRoot() != null) {
            if (!isLazyLoad) {
                initLazyLoad();
            } else {
                // 从不可见到可见
                onRestart();
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyLogManager.d(mTag, "onCreateView");
//        mRootView = inflater.inflate(onCreateFragmentViewId(), container, false);
        mBinding = DataBindingUtil.inflate(
                inflater,
                onCreateFragmentViewId(), container, false);


        initLoadSir(mBinding.getRoot(), "");
        if (mBaseLoadService != null) {
            mBaseLoadService.showSuccess();
        }
        return mBaseLoadService.getLoadLayout();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MyLogManager.d(mTag, "onActivityCreated" + "  isReplaceFragment: " + isReplaceFragment + " isFragmentVisible: " + isFragmentVisible);
        if (isReplaceFragment) {
            if (isFragmentVisible) {
                initLazyLoad();
            }
        } else {
            initLazyLoad();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MyLogManager.d(mTag, "onResume    " + "isReplaceFragment: " + isReplaceFragment + " isFragmentVisible: " + isFragmentVisible + " isLazyLoad:" + isLazyLoad);
//        if (isReplaceFragment) {
        if (isFragmentVisible && !isLazyLoad) {
            isLazyLoad = true;
            initData();
        }
       /* } else {
            if (!isLazyLoad) {
                isLazyLoad = true;
                initData();
            }
        }*/

    }

    @Override
    public void onPause() {
        super.onPause();
        MyLogManager.d(mTag, "onPause " + "isFragmentVisible: " + isFragmentVisible + " isLazyLoad:" + isLazyLoad);
        isLazyLoad = false;
    }

    /**
     * 初始化懒加载
     */
    protected void initLazyLoad() {
        if (!isLazyLoad) {
            isLazyLoad = true;
            initFragment();
        }
    }

    /**
     * 从可见的状态变成不可见状态，再从不可见状态变成可见状态时回调
     */
    @SuppressWarnings("all")
    protected void onRestart() {
        initData();
    }

    protected void initFragment() {
        initView();
        initData();
    }

    public void initData() {
        MyLogManager.d(mTag, "initData ");
    }


    /**
     * startActivity 方法优化
     */

    public void startActivity(Class<? extends Activity> cls) {
        startActivity(new Intent(mActivity, cls));
    }

    public void startActivityFinish(Class<? extends Activity> cls) {
        startActivityFinish(new Intent(mActivity, cls));
    }

    public void startActivityFinish(Intent intent) {
        startActivity(intent);
        finish();
    }

    /**
     * 打开新的 Activity
     *
     * @param activity 目标Activity
     * @param pairs    键值对
     */
    protected void startActivity(Class<? extends Activity> activity, Pair<String, Object>...
            pairs) {
        Intent intent = new Intent(mActivity, activity);
        // 填充数据
        fillIntent(intent, pairs);
        startActivity(intent);

    }


    /**
     * 填充intent数据，暂时只写了常用的一些数据格式，不常用的没写
     *
     * @param intent
     * @param pairs
     */
    private void fillIntent(Intent intent, Pair<String, Object>[] pairs) {

        if (pairs != null) {
            for (Pair<String, Object> pair : pairs) {
                Object value = pair.second;
                //判断不同的类型，进行强转和存放
                if (value instanceof Boolean) {
                    intent.putExtra(pair.first, (Boolean) value);
                }
                if (value instanceof Byte) {
                    intent.putExtra(pair.first, (Byte) value);
                }
                if (value instanceof Short) {
                    intent.putExtra(pair.first, (Short) value);
                }
                if (value instanceof Long) {
                    intent.putExtra(pair.first, (Long) value);
                }

                if (value instanceof Float) {
                    intent.putExtra(pair.first, (Float) value);
                }

                if (value instanceof Double) {
                    intent.putExtra(pair.first, (Double) value);
                }
                if (value instanceof Integer) {
                    intent.putExtra(pair.first, (Integer) value);
                }
                if (value instanceof String) {
                    intent.putExtra(pair.first, (String) value);
                }
                if (value instanceof Parcelable) {
                    intent.putExtra(pair.first, (Parcelable) value);
                }
                if (value instanceof Serializable) {
                    intent.putExtra(pair.first, (Serializable) value);
                }

            }
        }
    }

    /**
     * 销毁当前 Fragment 所在的 Activity
     */
    public void finish() {
        mActivity.finish();
        mActivity = null;
    }

    /**
     * 显示吐司
     */
    public void toast(CharSequence text) {
        ToastUtils.show(text);
    }

    public void toast(@StringRes int id) {
        ToastUtils.show(id);
    }

    public void toast(Object object) {
        ToastUtils.show(object);
    }


    //TODO  如何实现动态修改提示文字
    private void initLoadSir(View content, String title) {
        if (content != null) {
            if (mBaseLoadService != null) {
                mBaseLoadService.showSuccess();
                mBaseLoadService = null;
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
            mBaseLoadService = loadSir.register(content, (Callback.OnReloadListener) this::onReload);
        }
    }

    /**
     * 显示加载中
     */
    public void showLoading(View view) {
        contentView = view;
        initLoadSir(view, this.getString(R.string.common_loading));
        if (mBaseLoadService != null) {
            mBaseLoadService.showCallback(LoadingCallback.class);
        }
    }

    public void showLoading(View view, @StringRes int id) {
        contentView = view;
        initLoadSir(view, this.getString(id));
        if (mBaseLoadService != null) {
            mBaseLoadService.showCallback(LoadingCallback.class);
        }
    }

    public void showLoading(View view, String hintStr) {
        contentView = view;
        initLoadSir(view, hintStr);
        if (mBaseLoadService != null) {
            mBaseLoadService.showCallback(LoadingCallback.class);
        }
    }

    /**
     * 显示加载完成
     */
    public void showComplete() {
        if (mBaseLoadService != null) {
            mBaseLoadService.showSuccess();
        }
    }

    /**
     * 显示空提示
     */
    public void showEmpty() {
        if (mBaseLoadService != null) {
            mBaseLoadService.showCallback(EmptyCallback.class);
        }
    }

    /**
     * 显示错误提示
     */
    public void showError(boolean isNetworkAvailable) {
        if (mBaseLoadService != null) {
            if (isNetworkAvailable) {

                mBaseLoadService.showCallback(ErrorCallback.class);
            } else {
                mBaseLoadService.showCallback(TimeoutCallback.class);

            }
        }
    }


}

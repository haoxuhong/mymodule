package com.uweic.lib_base.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.uweic.lib_base.R;
import com.uweic.lib_base.databinding.ActivityBaseBinding;
import com.uweic.lib_base.loadsir.EmptyCallback;
import com.uweic.lib_base.loadsir.ErrorCallback;
import com.uweic.lib_base.loadsir.LoadingCallback;
import com.uweic.lib_base.loadsir.TimeoutCallback;
import com.uweic.lib_base.utils.ActivityStackManager;
import com.uweic.lib_dialog.action.ActivityAction;
import com.uweic.lib_dialog.action.BundleAction;
import com.uweic.lib_dialog.action.ClickAction;
import com.uweic.lib_dialog.action.HandlerAction;

import java.io.Serializable;

/**
 * Created by haoxuhong on 2019/12/23.
 *
 * @description: Activity 基类
 */
public abstract class BaseActivity<V extends ViewDataBinding> extends AppCompatActivity
        implements ActivityAction, ClickAction, HandlerAction, BundleAction {
    protected String TAG = this.getClass().getSimpleName();
    protected ActivityBaseBinding mBinding;
    protected LoadService mBaseLoadService;
    private View content;
    protected V mChildBinding;

    protected abstract int initContentViewId();//初始化内容布局

    protected abstract void initView();//初始化控件

    protected void onReload(View v) {
    }//重新加载

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.getInstance().onCreated(this);
        initActivity();
        //初始化沉浸式
        initImmersionBar();
        initSoftKeyboard();
    }

    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
                .statusBarColor(R.color.color_common_light_status_bar)
                .statusBarDarkFont(true)// 默认状态栏字体颜色为黑色
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题
                .init();
    }

    protected void initActivity() {
        initLayout();
        initView();
    }

    /**
     * 初始化布局  若不需要标题可重写这个方法自己实现
     */
    protected void initLayout() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_base);
        addContent();

    }

    private void addContent() {
        mChildBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                initContentViewId(), null, false);
//        content = View.inflate(this, initContentViewId(), null);
        content = mChildBinding.getRoot();
        if (content != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            mBinding.activityCommonContentFl.addView(content, params);

        }
    }

    /* public class BaseActivity<B extends ViewDataBinding>
              extends AppCompatActivity {
          protected B mBinding;

          @Override
          public void setContentView(int layoutResID) {
              mBinding = DataBindingUtil.inflate(
                      LayoutInflater.from(mContext),
                      layoutResID, null, false);
              super.setContentView(mBinding.getRoot());
          }*/
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
     * 初始化软键盘
     */
    protected void initSoftKeyboard() {
        // 点击外部隐藏软键盘，提升用户体验
        getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });
    }


    @Override
    public void finish() {
        hideSoftKeyboard();
        super.finish();
    }


    @Override
    protected void onDestroy() {
        // 移除和这个 Activity 相关的消息回调
        removeCallbacks();
        super.onDestroy();
        ActivityStackManager.getInstance().onDestroyed(this);
    }

    /**
     * 如果当前的 Activity（singleTop 启动模式） 被复用时会回调
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 设置为当前的 Intent，避免 Activity 被杀死后重启 Intent 还是最原先的那个
        setIntent(intent);
    }

    @Override
    public Bundle getBundle() {
        return getIntent().getExtras();
    }

    @Override
    public Context getContext() {
        return this;
    }

    /**
     * 和 setContentView 对应的方法
     */
    public ViewGroup getContentView() {
        return findViewById(Window.ID_ANDROID_CONTENT);
    }


    /////////////////界面状态管理/////////////


    /**
     * 显示加载中
     */
    public void showLoading(View view) {
        content = view;
        initLoadSir(content, this.getString(R.string.common_loading));
        if (mBaseLoadService != null) {
            mBaseLoadService.showCallback(LoadingCallback.class);
        }
    }

    public void showLoading(View view, @StringRes int id) {
        content = view;
        initLoadSir(content, this.getString(id));
        if (mBaseLoadService != null) {
            mBaseLoadService.showCallback(LoadingCallback.class);
        }
    }

    public void showLoading(View view, String hintStr) {
        content = view;
        initLoadSir(content, hintStr);
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

                mBaseLoadService.showCallback(TimeoutCallback.class);
            } else {
                mBaseLoadService.showCallback(ErrorCallback.class);
            }
        }
    }
    /////////////////界面状态管理/////////////


    ///////////////////////////title相关///////////////////////

    /**
     * 只有title
     */
    protected void initTopBarForOnlyTitle(String titleName, int titleCenterTextColor) {
        //标题设置
        mBinding.commonHeaderRl.setBackgroundColor(getTitleBackgroundColor() == 0 ? this.getResources().getColor(R.color.color_common_light_application_column) : getTitleBackgroundColor());
        mBinding.commonHeaderCenterTv.setVisibility(View.VISIBLE);
        mBinding.commonHeaderCenterTv.setText(titleName);
        mBinding.commonHeaderCenterTv.setTextColor(this.getResources().getColor(titleCenterTextColor));

    }

    private int titleBackgroundColor;

    public void setTitleBackgroundColor(int titleBackgroundColor) {
        this.titleBackgroundColor = titleBackgroundColor;
    }

    public int getTitleBackgroundColor() {
        return titleBackgroundColor;
    }

    /**
     * 只有左边返回按钮和Title
     */
    protected void initTopBarForLeft(int leftDrawableId, String titleName, int titleCenterTextColor) {
        initTopBarForOnlyTitle(titleName, titleCenterTextColor);
        //返回键
        mBinding.commonHeaderLeftArrowIv.setVisibility(View.VISIBLE);
        mBinding.commonHeaderLeftArrowIv.setImageResource(leftDrawableId);
        mBinding.commonHeaderLeftArrowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick(v);
            }
        });
    }

    /**
     * 初始化标题栏-带左边返回按钮 中间标题  右边文字
     */
    protected void initTopBarForBoth(int leftDrawableId, String titleName, int titleCenterTextColor, String rightText, int rightTextColor) {
        initTopBarForLeft(leftDrawableId, titleName, titleCenterTextColor);
        mBinding.commonHeaderRightTv.setVisibility(View.VISIBLE);
        mBinding.commonHeaderRightTv.setText(rightText);
        mBinding.commonHeaderRightTv.setTextColor(this.getResources().getColor(rightTextColor));
        mBinding.commonHeaderRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightClick(v);
            }
        });
    }

    /**
     * 初始化标题栏-带左边返回按钮 中间标题  右边图片
     */
    protected void initTopBarForBoth(int leftDrawableId, String titleName, int titleCenterTextColor, int rightDrawableId) {
        initTopBarForLeft(leftDrawableId, titleName, titleCenterTextColor);
        mBinding.commonHeaderRightIv.setVisibility(View.VISIBLE);
        mBinding.commonHeaderRightIv.setImageResource(rightDrawableId);
        mBinding.commonHeaderRightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightClick(v);
            }
        });
    }

    public void onBackClick(View view) {
        finish();
    }

    public void onRightClick(View view) {
    }
    ///////////////////////////title相关///////////////////////


    /////////////////显示吐司////////////
    public void toast(CharSequence text) {
        ToastUtils.show(text);
    }

    public void toast(@StringRes int id) {
        ToastUtils.show(id);
    }

    public void toast(Object object) {
        ToastUtils.show(object);
    }
    /////////////////显示吐司////////////


    /**
     * startActivity  不带参  并退出当前界面
     */
    public void startActivityFinish(Class<? extends Activity> cls) {
        startActivity(cls);
        finish();
    }

    /**
     * 打开新的 Activity   带参
     *
     * @param activity 目标Activity
     * @param pairs    键值对
     */
    protected void startActivity(Class<? extends Activity> activity, Pair<String, Object>... pairs) {
        Intent intent = new Intent(this, activity);
        // 填充数据
        fillIntent(intent, pairs);
        startActivity(intent);

    }

    /**
     * 打开 Activity  不带参
     *
     * @param activity
     */
    protected void startActivityForResult(Class<? extends Activity> activity, int requestCode) {
        startActivityForResult(new Intent(this, activity), requestCode);
    }


    /**
     * 打开新的 Activity  带参
     *
     * @param activity 目标Activity
     * @param pairs    键值对
     */
    protected void startActivityForResult(Class<? extends Activity> activity, int requestCode, Pair<String, Object>... pairs) {
        Intent intent = new Intent(this, activity);
        // 填充数据
        fillIntent(intent, pairs);
        startActivityForResult(intent, requestCode);
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
    /**调用
     * //普通
     launchActivity(DialogExampleActivity.class);
     //普通携带参数
     launchActivity(ToastExampleActivityActivity.class,
     new Pair<String, Object>("key1", "value1"),
     new Pair<String, Object>("key1", "value1"));
     //返回值
     launchActivityForResult(LoginActivity.class,200);

     // 返回值携带参数
     launchActivityForResult(LoginActivity.class,
     200,
     new Pair<String, Object>("key1", "value1"),
     new Pair<String, Object>("key1", "value1"));
     */


    /**
     * 隐藏软键盘
     */
    private void hideSoftKeyboard() {
        // 隐藏软键盘，避免软键盘引发的内存泄露
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null && manager.isActive(view)) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


}
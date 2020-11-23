package com.uweic.mymodule;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.hjq.toast.ToastInterceptor;
import com.hjq.toast.ToastUtils;
import com.kingja.loadsir.core.LoadSir;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.uweic.lib_base.BaseApplication;
import com.uweic.lib_base.loadsir.EmptyCallback;
import com.uweic.lib_base.loadsir.ErrorCallback;
import com.uweic.lib_base.loadsir.TimeoutCallback;
import com.uweic.lib_base.utils.MyLogManager;
import com.uweic.lib_common.constant.Constant;
import com.uweic.mymodule.common.network.CustomTokenInterceptor;
import com.uweic.mymodule.common.network.HandleErrorInterceptor;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.converter.GsonDiskConverter;

/**
 * Created by haoxuhong on 2020/8/21
 *
 * @description:
 */
public class MyApplication extends BaseApplication {
    //SmartRefreshLayout初始化配置
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.color_white, android.R.color.black);//全局设置主题颜色
                return new MaterialHeader(context).setColorSchemeResources(R.color.color_home_text_count);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initSDK(this);

    }


    /**
     * 初始化一些第三方框架
     */
    public void initSDK(Application application) {
        initToastUtils(application);
        initLoadSir();
        initNetwork(application);
    }

    private void initLoadSir() {
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new TimeoutCallback())
                .setDefaultCallback(null)
                .commit();
    }

    private void initToastUtils(Application application) {
        // 设置 Toast 拦截器
        ToastUtils.setToastInterceptor(new ToastInterceptor() {
            @Override
            public boolean intercept(Toast toast, CharSequence text) {
                boolean intercept = super.intercept(toast, text);
                if (intercept) {
                    MyLogManager.e("Toast", "空 Toast");
                } else {
                    MyLogManager.i("Toast", text.toString());
                }
                return intercept;
            }
        });
        // 吐司工具类
        ToastUtils.init(application);
    }

    private static void initNetwork(Application application) {
        String baseUrl = null;
        boolean isDebug = false;
        if (BuildConfig.DEBUG) {
//            baseUrl = Constant.COMMON_REQUEST_PATH_DEBUG;
            baseUrl = Constant.COMMON_REQUEST_PATH;
            isDebug = true;
        } else {
            baseUrl = Constant.COMMON_REQUEST_PATH;
            isDebug = false;
        }
        //网络封装库
        EasyHttp.init(application);

        EasyHttp.getInstance()
                .setBaseUrl(baseUrl)
                .debug("EasyHttp", isDebug)
                .setReadTimeOut(15 * 1000)
                .setWriteTimeOut(15 * 100)
                .setConnectTimeout(15 * 100)
                //全局设置自定义缓存保存转换器，主要针对自定义RxCache缓存
                .setCacheDiskConverter(new GsonDiskConverter())//默认缓存使用序列化转化
                .addInterceptor(new HandleErrorInterceptor())//添加异常拦截器
                .addInterceptor(new CustomTokenInterceptor());//添加参数签名拦截器
    }
}

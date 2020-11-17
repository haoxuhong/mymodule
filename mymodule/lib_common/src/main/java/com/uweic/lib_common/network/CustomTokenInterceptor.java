package com.uweic.lib_common.network;

import com.zhouyou.http.interceptor.BaseDynamicInterceptor;

import java.util.TreeMap;

/**
 * Created by haoxuhong on 2020/7/8
 *
 * @description:
 */
public class CustomTokenInterceptor extends BaseDynamicInterceptor<CustomTokenInterceptor> {
    @Override
    public TreeMap<String, String> dynamic(TreeMap<String, String> dynamicMap) {

        if (isAccessToken()) {//是否添加token
//            String acccess = TokenManager.getInstance().getAuthModel().getAccessToken();
    /*        String token = SpUtil.getString(MyApplication.getInstance(), Constant.SP_TOKEN, "");
            dynamicMap.put(Constant.SP_TOKEN, token);*/
        }

        //HttpLog.i("dynamicMap:" + dynamicMap.toString());
        return dynamicMap;//dynamicMap:是原有的全局参数+局部参数+新增的动态参数
    }


}

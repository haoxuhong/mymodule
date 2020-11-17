package com.uweic.lib_common.network;


import android.text.TextUtils;

import com.zhouyou.http.interceptor.ResponseBodyInterceptor;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by haoxuhong on 2019/9/18.
 *
 * @description: 异常拦截器  data字段没有或值为null 如:{"msg":"success","code":0}  或  {"msg":"success","code":0,"data":null}
 */

public class HandleErrorInterceptor extends ResponseBodyInterceptor {
    @Override
    public Response intercept(Chain chain, Response response, String url, String body) {
        JSONObject jsonObject = null;
//        body = "{ \"code\": 0, \"msg\": \"string\"}";
//        body = "{\"msg\":\"success\",\"code\":0,\"data\":null}";
//        body = "{\"msg\":\"success\",\"code\":0,\"data\":[]}";
/* 测试实体类里有的string类型的属性值为null时

        body = "{\"msg\":\"success\",\"code\":0,\"data\":{\"POWERS_1\":0,\"ELECTRICITY_1\":null,\"allPower\":\"54.539476\"}}";



        Response clone = response.newBuilder().code(200) // 其实code可以随便给 code，protocol，message，body缺一不可。
                .protocol(Protocol.HTTP_1_1)
                .message("")
                .body(ResponseBody.create(response.body().contentType(), body))
                .request(chain.request())
                .build();
        return clone;*/


        try {
            jsonObject = new JSONObject(body);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (response.code() == 200 && jsonObject != null) {


            String data = jsonObject.optString("data");
            if (TextUtils.isEmpty(data) || "null".equals(data)) {
                String code = jsonObject.optString("code");
                String msg = jsonObject.optString("msg");
                String s = "{" + "msg" + ":" + msg + "," + "code" + ":" + code + "," + "data" + ":" + "{}" + "}";
                Response clone = response.newBuilder().code(200) // 其实code可以随便给 code，protocol，message，body缺一不可。
                        .protocol(Protocol.HTTP_1_1)
                        .message("")
                        .body(ResponseBody.create(response.body().contentType(), s))
                        .request(chain.request())
                        .build();
                return clone;
            }

        }
        return response;

    }
}
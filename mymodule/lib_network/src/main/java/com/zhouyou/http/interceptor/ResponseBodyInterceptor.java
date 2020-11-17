package com.zhouyou.http.interceptor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

/**
 * Created by haoxuhong on 2020/7/20
 *
 * @description:   解决服务端返回值  response.body().string()只能请求一次,请求过后,就会关闭,再次调用response.body().string()就会报close异常
 *     返回个String body
 */
public abstract class ResponseBodyInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String url = request.url().toString();
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            long contentLength = responseBody.contentLength();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();

            if ("gzip".equals(response.headers().get("Content-Encoding"))) {
                GzipSource gzippedResponseBody = new GzipSource(buffer.clone());
                buffer = new Buffer();
                buffer.writeAll(gzippedResponseBody);
            }

            MediaType contentType = responseBody.contentType();
            Charset charset;
            if (contentType == null || contentType.charset(StandardCharsets.UTF_8) == null) {
                charset = StandardCharsets.UTF_8;
            } else {
                charset = contentType.charset(StandardCharsets.UTF_8);
            }

            if (charset != null && contentLength != 0L) {
                return intercept(chain,response, url, buffer.clone().readString(charset));
            }
        }
        return response;
    }

    public abstract Response intercept(Chain chain,Response response, String url, String body);
}



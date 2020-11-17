package com.uweic.webview.webviewprocess;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.uweic.webview.bean.JsParam;
import com.uweic.webview.webviewprocess.settings.WebViewDefaultSettings;
import com.uweic.webview.WebViewCallBack;

import com.uweic.webview.webviewprocess.webchromeclient.MyWebChromeClient;
import com.uweic.webview.webviewprocess.webviewclient.MyWebViewClient;

public class BaseWebView extends WebView {
    public static final String TAG = "mywebview";

    public BaseWebView(Context context) {
        super(context);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        WebViewProcessCommandDispatcher.getInstance().initAidlConnection();
        WebViewDefaultSettings.getInstance().setSettings(this);
        addJavascriptInterface(this, "mywebview");
    }

    public void registerWebViewCallBack(WebViewCallBack webViewCallBack) {
        setWebViewClient(new MyWebViewClient(webViewCallBack));
        setWebChromeClient(new MyWebChromeClient(webViewCallBack));
    }

    @JavascriptInterface
    public void takeNativeAction(final String jsParam) {
        Log.i(TAG, jsParam);
        if (!TextUtils.isEmpty(jsParam)) {
            final JsParam jsParamObject = new Gson().fromJson(jsParam, JsParam.class);
            if (jsParamObject != null) {
                WebViewProcessCommandDispatcher.getInstance().executeCommand(jsParamObject.name, new Gson().toJson(jsParamObject.param), this);
            }
        }
    }

    public void handleCallback(final String callbackname, final String response){
        if(!TextUtils.isEmpty(callbackname) && !TextUtils.isEmpty(response)){
            post(new Runnable() {
                @Override
                public void run() {
                    String jscode = "javascript:myjs.callback('" + callbackname + "'," + response + ")";
                    Log.e("xxxxxx", jscode);
                    evaluateJavascript(jscode, null);
                }
            });
        }
    }
}

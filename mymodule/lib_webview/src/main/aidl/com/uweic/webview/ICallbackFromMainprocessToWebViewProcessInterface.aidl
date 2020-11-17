// ICallbackFromMainprocessToWebViewProcessInterface.aidl
package com.uweic.webview;

interface ICallbackFromMainprocessToWebViewProcessInterface {
    void onResult(String callbackname, String response);
}

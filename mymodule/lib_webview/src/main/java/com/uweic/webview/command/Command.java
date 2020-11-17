package com.uweic.webview.command;

import com.uweic.webview.ICallbackFromMainprocessToWebViewProcessInterface;

import java.util.Map;

public interface Command {
    String name();
    void execute(Map parameters, ICallbackFromMainprocessToWebViewProcessInterface callback);
}

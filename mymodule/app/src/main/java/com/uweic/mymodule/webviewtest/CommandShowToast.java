package com.uweic.mymodule.webviewtest;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.auto.service.AutoService;
import com.uweic.lib_base.BaseApplication;
import com.uweic.webview.ICallbackFromMainprocessToWebViewProcessInterface;
import com.uweic.webview.command.Command;

import java.util.Map;

@AutoService({Command.class})
public class CommandShowToast implements Command {
    @Override
    public String name() {
        return "showToast";
    }

    @Override
    public void execute(final Map parameters, ICallbackFromMainprocessToWebViewProcessInterface callback) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseApplication.sApplication, String.valueOf(parameters.get("message")), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

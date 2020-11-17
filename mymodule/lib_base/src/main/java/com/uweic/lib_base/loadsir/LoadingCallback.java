package com.uweic.lib_base.loadsir;

import android.content.Context;
import android.view.View;

import androidx.annotation.StyleRes;

import com.kingja.loadsir.callback.Callback;
import com.uweic.lib_base.R;
import com.uweic.lib_base.utils.MyLogManager;
import com.uweic.lib_dialog.widget.SmartTextView;

/**
 * Description:TODO
 * Create Time:2017/9/4 10:22
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */

public class LoadingCallback extends Callback {
    private String title;

    public LoadingCallback(Builder builder) {
        this.title = builder.title;
    }

    @Override
    protected int onCreateView() {
        return R.layout.layout_loading;
    }

    @Override
    protected void onViewCreate(Context context, View view) {
        super.onViewCreate(context, view);
        SmartTextView msgTV = view.findViewById(R.id.tv_wait_message);
        msgTV.setText(title);
    }

    @Override
    public boolean getSuccessVisible() {
        //是否在显示Callback视图的时候显示原始图(SuccessView)，返回true显示，false隐藏

        return true;
    }

    @Override
    protected boolean onReloadEvent(Context context, View view) {
        return true;
    }

    public static class Builder {

        private String title;


        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }


        public LoadingCallback build() {
            return new LoadingCallback(this);
        }
    }
}

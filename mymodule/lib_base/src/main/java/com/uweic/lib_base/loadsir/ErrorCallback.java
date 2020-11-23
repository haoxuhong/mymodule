package com.uweic.lib_base.loadsir;


import android.content.Context;
import android.view.View;

import com.kingja.loadsir.callback.Callback;
import com.uweic.lib_base.R;

/**
 * Description:TODO
 * Create Time:2017/9/4 10:20
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */

public class ErrorCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.layout_error;
    }
    @Override
    protected boolean onReloadEvent(Context context, View view) {
        return false;
    }
}

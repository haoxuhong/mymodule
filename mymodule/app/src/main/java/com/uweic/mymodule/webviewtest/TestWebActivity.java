package com.uweic.mymodule.webviewtest;

import android.view.View;

import com.uweic.lib_base.autoservice.MyServiceLoader;
import com.uweic.lib_common.autoservice.IWebViewService;
import com.uweic.lib_common.mvp.MvpActivity;
import com.uweic.mymodule.R;
import com.uweic.mymodule.databinding.ActivityTestWebBinding;

/**
 * Created by haoxuhong on 2020/9/4
 *
 * @description:
 */
public class TestWebActivity extends MvpActivity<ActivityTestWebBinding> {
    @Override
    protected int initContentViewId() {
        return R.layout.activity_test_web;
    }

    @Override
    protected void initView() {
        mChildBinding.webviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IWebViewService webviewService = MyServiceLoader.load(IWebViewService.class);
                if (webviewService != null) {
//                    webviewService.startWebViewActivity(TestWebActivity.this, "http://www.baidu.com", "baidu", true);

                    webviewService.startDemoHtml(TestWebActivity.this);

                }

            }
        });
    }
}

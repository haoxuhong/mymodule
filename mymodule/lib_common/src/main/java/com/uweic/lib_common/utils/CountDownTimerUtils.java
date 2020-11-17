package com.uweic.lib_common.utils;

import android.os.CountDownTimer;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by haoxuhong on 2020/3/26.
 *
 * @description: 发送验证码实现倒计时功能
 */

public class CountDownTimerUtils extends CountDownTimer {
    private AppCompatTextView mTextView; //显示倒计时的文字

    /**
     * @param mTextView         显示倒计时的文字
     * @param millisInFuture    从开始调用start()到倒计时完成
     *                          *              并onFinish()方法被调用的毫秒数。（译者注：倒计时时间，单位毫秒）
     * @param countDownInterval 接收onTick(long)回调的间隔时间。（译者注：单位毫秒）
     */
    public CountDownTimerUtils(AppCompatTextView mTextView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = mTextView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false); //设置不可点击
        mTextView.setText(millisUntilFinished / 1000 + "秒后重发"); //设置倒计时时间
    }

    @Override
    public void onFinish() {
        mTextView.setText("重新获取验证码");
        mTextView.setClickable(true);//重新获得点击
    }
}

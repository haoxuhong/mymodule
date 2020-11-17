package com.uweic.lib_dialog;

import android.view.Gravity;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;


import com.uweic.lib_dialog.widget.SettingBar;
import com.uweic.lib_dialog.base.BaseDialog;

import java.util.List;

/**
 * Created by haoxuhong on 2019/11/22.
 *
 * @description: dialog最终使用封装
 */
public class CommonDialogUtils {

    /**
     * 公共的居中选择框
     *
     * @param activity
     * @param data
     * @param rightSb
     */


    public static void CommonCenterSelectDialog(FragmentActivity activity, List data, SettingBar rightSb) {
        // 居中选择框
        new MenuDialog.Builder(activity)
                .setGravity(Gravity.CENTER)
                .setList(data)
                .setListener(new MenuDialog.OnListener<String>() {

                    @Override
                    public void onSelected(BaseDialog dialog, int position, String string) {

                        rightSb.setRightText(string);
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
//                                toast("取消了");
                    }
                })
                .show();
    }

    /**
     * 公共的输入对话框
     *
     * @param activity
     * @param rightSb
     */


    public static void CommonInputDialog(FragmentActivity activity, CharSequence titleStr, SettingBar rightSb) {
        new InputDialog.Builder(activity)
                // 标题可以不用填写
                .setTitle(titleStr)
                .setContent(rightSb.getRightText())
                .setListener(new InputDialog.OnListener() {

                    @Override
                    public void onConfirm(BaseDialog dialog, String content) {
                        if (!rightSb.getRightText().equals(content)) {
                            rightSb.setRightText(content);
                        }
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                    }
                })
                .show();
    }

    /**
     * 公共的日期对话框
     *
     * @param activity
     * @param textView
     */

    public static void CommonDateDialog(FragmentActivity activity, View textView) {
        new DateDialog.Builder(activity)
                .setListener(new DateDialog.OnListener() {
                    @Override
                    public void onSelected(BaseDialog dialog, int year, int month, int day) {
                        String timeStr = year + "-" + (month < 10 ? "0" : "") + month + "-" + (day < 10 ? "0" : "") + day;
                        if (textView instanceof AppCompatTextView) {
                            AppCompatTextView textView1 = (AppCompatTextView) textView;
                            textView1.setText(timeStr);
                        } else if (textView instanceof SettingBar) {
                            SettingBar textView1 = (SettingBar) textView;
                            textView1.setRightText(timeStr);
                        }
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {

                    }
                }).show();
    }

    /**
     * 公共的日期时间对话框
     *
     * @param activity
     * @param textView
     */


    public static void CommonDateTimeDialog(FragmentActivity activity, View textView) {
        new DateTimeDialog.Builder(activity)
                .setListener(new DateTimeDialog.OnListener() {
                    @Override
                    public void onSelected(BaseDialog dialog, int year, int month, int day, int hour, int minute, int second) {
                        String timeStr = year + "-" + (month < 10 ? "0" : "") + month + "-" + (day < 10 ? "0" : "") + day + " " + (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute + ":" + (second < 10 ? "0" : "") + second;
                        if (textView instanceof AppCompatTextView) {
                            AppCompatTextView textView1 = (AppCompatTextView) textView;
                            textView1.setText(timeStr);
                        } else if (textView instanceof SettingBar) {
                            SettingBar textView1 = (SettingBar) textView;
                            textView1.setRightText(timeStr);
                        }
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {

                    }
                }).show();
    }

}

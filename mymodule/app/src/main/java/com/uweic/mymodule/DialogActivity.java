package com.uweic.mymodule;

import android.view.Gravity;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.uweic.lib_dialog.AddressDialog;
import com.uweic.lib_dialog.DateDialog;
import com.uweic.lib_dialog.HintDialog;
import com.uweic.lib_dialog.InputDialog;
import com.uweic.lib_dialog.MenuDialog;
import com.uweic.lib_dialog.MessageDialog;
import com.uweic.lib_dialog.PayPasswordDialog;
import com.uweic.lib_dialog.SelectDialog;
import com.uweic.lib_dialog.TimeDialog;
import com.uweic.lib_dialog.UpdateDialog;
import com.uweic.lib_dialog.WaitDialog;
import com.uweic.lib_dialog.base.BaseDialog;
import com.uweic.mymodule.common.mvp.MvpActivity;
import com.uweic.mymodule.databinding.ActivityDialogBinding;
import com.uweic.mymodule.utils.CustomParkingDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DialogActivity extends MvpActivity<ActivityDialogBinding> {
    @Override
    protected int initContentViewId() {
        return R.layout.activity_dialog;
    }

    @Override
    protected void initView() {
        setOnClickListener(mChildBinding.btnDialogMessage, mChildBinding.btnDialogInput, mChildBinding.btnDialogBottomMenu, mChildBinding.btnDialogCenterMenu, mChildBinding.btnDialogSingleSelect,
                mChildBinding.btnDialogMoreSelect, mChildBinding.btnDialogSucceedToast, mChildBinding.btnDialogFailToast, mChildBinding.btnDialogWait, mChildBinding.btnDialogWarnToast,
                mChildBinding.btnDialogPay, mChildBinding.btnDialogAddress, mChildBinding.btnDialogDate, mChildBinding.btnDialogTime, mChildBinding.btnDialogUpdate, mChildBinding.btnDialogShare, mChildBinding.btnDialogSafe, mChildBinding.btnDialogCustom);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog_message:
                // 消息对话框
                new MessageDialog.Builder(this)
                        // 标题可以不用填写
                        .setTitle("我是标题")
                        // 内容必须要填写
                        .setMessage("我是内容")
                        // 确定按钮文本
                        .setConfirm(getString(R.string.common_confirm))
                        // 设置 null 表示不显示取消按钮
                        .setCancel(getString(R.string.common_cancel))
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setListener(new MessageDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                toast("确定了");
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        })
                        .show();
                break;
            case R.id.btn_dialog_input:
                // 输入对话框
                new InputDialog.Builder(this)
                        // 标题可以不用填写
                        .setTitle("我是标题")
                        // 内容可以不用填写
                        .setContent("我是内容")
                        // 提示可以不用填写
                        .setHint("我是提示")
                        // 确定按钮文本
                        .setConfirm(getString(R.string.common_confirm))
                        // 设置 null 表示不显示取消按钮
                        .setCancel(getString(R.string.common_cancel))
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setListener(new InputDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                toast("确定了：" + content);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        })
                        .show();
                break;
            case R.id.btn_dialog_bottom_menu:
                List<String> data = new ArrayList<>();
                for (int i = 0; i < 100; i++) {
                    data.add("我是数据" + (i + 1));
                }
                // 底部选择框
                new MenuDialog.Builder(this)

                        .setTitle("请绑定车场")
                        .setList(data)
                        .setListener(new MenuDialog.OnListener<String>() {

                            @Override
                            public void onSelected(BaseDialog dialog, int position, String string) {
                                toast("位置：" + position + "，文本：" + string);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        })
                        .show();
                break;
            case R.id.btn_dialog_center_menu:
                List<String> data1 = new ArrayList<>();
                for (int i = 0; i < 100; i++) {
                    data1.add("我是数据" + (i + 1));
                }
                // 居中选择框
                new MenuDialog.Builder(this)
                        .setGravity(Gravity.CENTER)
                        // 设置 null 表示不显示取消按钮
                        //.setCancel(null)
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setList(data1)
                        .setListener(new MenuDialog.OnListener<String>() {

                            @Override
                            public void onSelected(BaseDialog dialog, int position, String string) {
                                toast("位置：" + position + "，文本：" + string);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        })
                        .show();
                break;
            case R.id.btn_dialog_single_select:
                // 单选对话框
                new SelectDialog.Builder(this)
                        .setTitle("请选择你的性别")
                        .setList("男", "女")
                        // 设置单选模式
                        .setSingleSelect()
                        // 设置默认选中
                        .setSelect(0)
                        .setListener(new SelectDialog.OnListener<String>() {

                            @Override
                            public void onSelected(BaseDialog dialog, HashMap<Integer, String> data) {
                                toast("确定了：" + data.toString());
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        })
                        .show();
                break;
            case R.id.btn_dialog_more_select:
                // 多选对话框
                new SelectDialog.Builder(this)
                        .setTitle("请选择工作日")
                        .setList("星期一", "星期二", "星期三", "星期四", "星期五")
                        // 设置最大选择数
                        .setMaxSelect(3)
                        // 设置默认选中
                        .setSelect(2, 3, 4)
                        .setListener(new SelectDialog.OnListener<String>() {

                            @Override
                            public void onSelected(BaseDialog dialog, HashMap<Integer, String> data) {
                                toast("确定了：" + data.toString());
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        })
                        .show();
                break;
            case R.id.btn_dialog_succeed_toast:
                // 成功对话框
                new HintDialog.Builder(this)
                        .setIcon(HintDialog.ICON_FINISH)
                        .setMessage("完成")
                        .show();
                break;
            case R.id.btn_dialog_fail_toast:
                // 失败对话框
                new HintDialog.Builder(this)
                        .setIcon(HintDialog.ICON_ERROR)
                        .setMessage("错误")
                        .show();
                break;
            case R.id.btn_dialog_warn_toast:
                // 警告对话框
                new HintDialog.Builder(this)
                        .setIcon(HintDialog.ICON_WARNING)
                        .setMessage("警告")
                        .show();
                break;
            case R.id.btn_dialog_wait:
                // 等待对话框
                final BaseDialog waitDialog = new WaitDialog.Builder(this)
                        // 消息文本可以不用填写
                        .setMessage(getString(R.string.common_loading))
                        .show();
                postDelayed(waitDialog::dismiss, 2000);
                break;
            case R.id.btn_dialog_pay:
                // 支付密码输入对话框
                new PayPasswordDialog.Builder(this)
                        .setTitle(getString(R.string.pay_title))
                        .setSubTitle("用于购买一个女盆友")
                        .setMoney("￥ 100.00")
                        //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                        .setListener(new PayPasswordDialog.OnListener() {

                            @Override
                            public void onCompleted(BaseDialog dialog, String password) {
                                toast(password);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        })
                        .show();
                break;
            case R.id.btn_dialog_address:
                // 选择地区对话框
                new AddressDialog.Builder(this)
                        .setTitle(getString(R.string.address_title))
                        // 设置默认省份
                        //.setProvince("广东省")
                        // 设置默认城市（必须要先设置默认省份）
                        //.setCity("广州市")
                        // 不选择县级区域
                        //.setIgnoreArea()
                        .setListener(new AddressDialog.OnListener() {

                            @Override
                            public void onSelected(BaseDialog dialog, String province, String city, String area) {
                                toast(province + city + area);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        })
                        .show();
                break;
            case R.id.btn_dialog_date:
                // 日期选择对话框
                new DateDialog.Builder(this)
                        .setTitle(getString(R.string.date_title))
                        // 确定按钮文本
                        .setConfirm(getString(R.string.common_confirm))
                        // 设置 null 表示不显示取消按钮
                        .setCancel(getString(R.string.common_cancel))
                        // 设置日期
                        //.setDate("2018-12-31")
                        //.setDate("20181231")
                        //.setDate(1546263036137)
                        // 设置年份
                        //.setYear(2018)
                        // 设置月份
                        //.setMonth(2)
                        // 设置天数
                        //.setDay(20)
                        // 不选择天数
                        //.setIgnoreDay()
                        .setListener(new DateDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, int year, int month, int day) {
                                toast(year + getString(R.string.common_year) + month + getString(R.string.common_month) + day + getString(R.string.common_day));

                                // 如果不指定时分秒则默认为现在的时间
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                // 月份从零开始，所以需要减 1
                                calendar.set(Calendar.MONTH, month - 1);
                                calendar.set(Calendar.DAY_OF_MONTH, day);
                                toast("时间戳：" + calendar.getTimeInMillis());
                                //toast(new SimpleDateFormat("yyyy年MM月dd日 kk:mm:ss").format(calendar.getTime()));
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        })
                        .show();
                break;
            case R.id.btn_dialog_time:
                // 时间选择对话框
                new TimeDialog.Builder(this)
                        .setTitle(getString(R.string.time_title))
                        // 确定按钮文本
                        .setConfirm(getString(R.string.common_confirm))
                        // 设置 null 表示不显示取消按钮
                        .setCancel(getString(R.string.common_cancel))
                        // 设置时间
                        //.setTime("23:59:59")
                        //.setTime("235959")
                        // 设置小时
                        //.setHour(23)
                        // 设置分钟
                        //.setMinute(59)
                        // 设置秒数
                        //.setSecond(59)
                        // 不选择秒数
                        //.setIgnoreSecond()
                        .setListener(new TimeDialog.OnListener() {

                            @Override
                            public void onSelected(BaseDialog dialog, int hour, int minute, int second) {
                                toast(hour + getString(R.string.common_hour) + minute + getString(R.string.common_minute) + second + getString(R.string.common_second));

                                // 如果不指定年月日则默认为今天的日期
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, second);
                                toast("时间戳：" + calendar.getTimeInMillis());
                                //toast(new SimpleDateFormat("yyyy年MM月dd日 kk:mm:ss").format(calendar.getTime()));
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        })
                        .show();
                break;
            case R.id.btn_dialog_share:
                new BaseDialog.Builder(this)
                        .setContentView(R.layout.dialog_custom_parking)
                        .setAnimStyle(BaseDialog.ANIM_BOTTOM)
                        .setGravity(Gravity.BOTTOM)
                        .setOnClickListener(R.id.dialog_custom_parking_close_iv, (BaseDialog.OnClickListener<AppCompatImageView>) (dialog, view) -> dialog.dismiss())
                        .setOnClickListener(R.id.dialog_custom_parking_confirm_btn, (BaseDialog.OnClickListener<AppCompatButton>) (dialog, view) -> dialog.dismiss())
                        .show();


                break;
            case R.id.btn_dialog_update:
                // 升级对话框
                new UpdateDialog.Builder(this)
                        // 版本名
                        .setVersionName("5.2.0")
                        .setCancelable(true)//可以取消
                        // 是否强制更新
                        .setForceUpdate(true)
                        // 更新日志
                        .setUpdateLog("到底更新了啥\n到底更新了啥\n到底更新了啥\n到底更新了啥\n到底更新了啥")
                        // 下载 url
                        .setDownloadUrl("https://dldir1.qq.com/weixin/android/weixin7014android1660.apk")
                        .setListener(() -> checkVersionAfter())
                        .show();



                break;
            case R.id.btn_dialog_safe:
                toast("点击了safe");
                new CustomParkingDialog.Builder(this)
                        .setParkingType(true)
                        .setListener(new CustomParkingDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                toast("true点击了确认按钮");
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("true点击了取消按钮");
                            }
                        }).show();
                break;
            case R.id.btn_dialog_custom:
                toast("点击了custom");
                new CustomParkingDialog.Builder(this)
                        .setParkingType(false)
                        .setListener(new CustomParkingDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                toast("false点击了确认按钮");
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("false点击了取消按钮");
                            }
                        }).show();
                break;
            default:
                break;
        }

    }


    /**
     * 比较版本之后要执行的代码
     */
    private void checkVersionAfter(){

    }
}

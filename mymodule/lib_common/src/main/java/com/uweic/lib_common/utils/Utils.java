package com.uweic.lib_common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.Selection;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.hjq.toast.ToastUtils;
import com.uweic.lib_base.utils.MyLogManager;
import com.uweic.lib_common.constant.Constant;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by haoxuhong on 2019/9/23.
 *
 * @description: 工具类
 * 网络判断   判断手机号码方法  获得字母和数字组成的随机数  获取手机状态栏的高度判断用户权限
 */
public class Utils {

    public static boolean isHavePermission(Context context, Integer integer) {
        List<Integer> menuList = SpUtil.getDataListI(context, Constant.SP_PERMISSION_LIST);
        if (menuList == null || menuList.size() == 0) {
            return false;
        }

        return menuList.contains(integer);
    }

    /**
     * 网络不可用时的提示信息
     *
     * @param context
     */
    public static void networkNoConnected(Context context) {
        if (!isNetworkConnected(context)) {
            ToastUtils.show("您的网络有问题，请检查网络");
            return;
        }
    }

    /**
     * 功能说明：判断网络是否可用
     *
     * @param context
     * @return boolean 返回类型 true为可用，false为不可用
     * @throws
     * @throws
     */
    private static boolean isNetworkConnected(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static final int NO_NETWORK = 0;
    public static final int NETWORK_CLOSED = 1;
    public static final int NETWORK_ETHERNET = 2;
    public static final int NETWORK_WIFI = 3;
    public static final int NETWORK_MOBILE = 4;
    public static final int NETWORK_UNKNOWN = -1;

    /**
     * 判断当前网络类型-1为未知网络0为没有网络连接1网络断开或关闭2为以太网3为WiFi4为2G5为3G6为4G
     */
    public static int getNetworkType(Context context) {
        //改为context.getApplicationContext()，防止在Android 6.0上发生内存泄漏
        ConnectivityManager connectMgr = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectMgr == null) {
            return NO_NETWORK;
        }

        NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            // 没有任何网络
            return NO_NETWORK;
        }
        if (!networkInfo.isConnected()) {
            // 网络断开或关闭
            return NETWORK_CLOSED;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
            // 以太网网络
            return NETWORK_ETHERNET;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            // wifi网络，当激活时，默认情况下，所有的数据流量将使用此连接
            return NETWORK_WIFI;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            // 移动数据连接,不能与连接共存,如果wifi打开，则自动关闭
            switch (networkInfo.getSubtype()) {
                // 2G
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    // 3G
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    // 4G
                case TelephonyManager.NETWORK_TYPE_LTE:
                    // 5G
//                case TelephonyManager.NETWORK_TYPE_NR:
                    return NETWORK_MOBILE;
            }
        }
        // 未知网络
        return NETWORK_UNKNOWN;
    }

    /**
     * 判断手机号码方法
     * [^4,\D]表示:除4以外的任意数字
     * \d{8}匹配8位数字
     * 2019.11.11更新
     * 中国移动:134 135 136 137 138 139  147   150 151 152 157 158 159  178  182 183 184 187 188
     * 中国电信:133  153   173 177   180 181 189   199
     * 中国联通:130 131 132  145    155  156  175 176 185 186
     * 全球星：1349
     * 虚拟运营商：170
     *
     * @param mobiles 手机号
     * @return boolean
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[0,3,5-8])|(18[0-9])|(19[9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }

    public static boolean isEmailNO(String email) {
        boolean matches = Patterns.EMAIL_ADDRESS.matcher(email).matches();//用的是android自带的patterns

        return matches;
    }


    /**
     * 获得字母和数字组成的随机数
     * 随机数长度可以自定义,修改length值就行
     *
     * @return
     */
    public static String getCharAndNumr() {
        String val = "";
        int length = 32;
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字

            if ("char".equalsIgnoreCase(charOrNum)) // 字符串
            {
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; //取得大写字母还是小写字母
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) // 数字
            {
                val += String.valueOf(random.nextInt(10));
            }
        }
        MyLogManager.d("数据生成的随机数", val);
        return val;


    }

    /**
     * 获取手机状态栏的高度
     *
     * @return 状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c;
        Object obj;
        Field field;
        int x, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    public static void passwordShowOrHide(ImageView passwordHideIv, ImageView passwordShowIv, EditText passwordEt, boolean isOnclickHidePassword) {
        if (isOnclickHidePassword) {
            passwordHideIv.setVisibility(View.GONE);
            passwordShowIv.setVisibility(View.VISIBLE);
            passwordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);//显示密码
            Selection.setSelection(passwordEt.getText(), passwordEt.getText().length()); // 输入框光标一直在输入文本后面

        } else {
            passwordShowIv.setVisibility(View.GONE);
            passwordHideIv.setVisibility(View.VISIBLE);
            passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);//隐藏密码
            Selection.setSelection(passwordEt.getText(), passwordEt.getText().length()); // 输入框光标一直在输入文本后面

        }
    }


    /**
     * 类型转换    int--->string
     *
     * @param i
     * @return
     */
    public static String int2Str(int i) {
        return String.valueOf(i);
    }

    /**
     * 类型转换    string--->int
     *
     * @param s
     * @return
     */
    public static int str2Int(String s) {
        return Integer.parseInt(s);
    }

    /**
     * 类型转换    float--->string
     *
     * @param i
     * @return
     */
    public static String float2Str(float i) {
        return String.valueOf(i);
    }

    /**
     * 类型转换    string--->float
     *
     * @param s
     * @return
     */
    public static float str2Float(String s) {
        return Float.parseFloat(s);
    }

    /**
     * 判断对象为空的方法：
     *
     * @param object
     * @return
     */
    public static boolean isEmptyObject(Object object) {

        return object == null || "".equals(object);

    }

    /**
     * 判断列表list为空的方法：
     *
     * @param collection
     * @return
     */
    public static boolean isEmptyList(Collection<?> collection) {

        return collection == null || collection.isEmpty();

    }

    /**
     * 判断Map为空的方法
     *
     * @param map
     * @return
     */
    public static boolean isEmptyMap(Map<?, ?> map) {

        return map == null || isEmptyList(map.keySet());

    }

    /**
     * 保留指定的小数点,不四舍五入
     *
     * @param dataStr     数字
     * @param placesCount 保留位数
     * @return 处理后的数据
     */
    public static String reservedDataPlaces(String dataStr, int placesCount) {
        String formatStr = "#.###";
        if (placesCount == 3) {
            formatStr = "#.###";
        }
        DecimalFormat decimalFormat = new DecimalFormat(formatStr);
        decimalFormat.setRoundingMode(RoundingMode.DOWN);

        return decimalFormat.format(new BigDecimal(dataStr));
    }
}

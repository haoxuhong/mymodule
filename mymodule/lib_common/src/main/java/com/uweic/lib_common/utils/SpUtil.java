package com.uweic.lib_common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uweic.lib_common.constant.Constant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpUtil {


    private static SharedPreferences getSp(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constant.SP_NAME_USER,
                Context.MODE_PRIVATE);
        return sp;
    }


    /**
     * 根据key清空value
     *
     * @param context 上下文
     * @param key     字符串的键
     */
    public static void removeKey(Context context, String key) {
        getSp(context).edit().remove(key).commit();

    }
    /**
     * 根据清空所有数据
     *
     * @param context 上下文
     */
    public static void clearData(Context context){
        getSp(context).edit().clear().commit();
    }
    /**
     * 存入字符串
     *
     * @param context 上下文
     * @param key     字符串的键
     * @param value   字符串的值
     */
    public static void putString(Context context, String key, String value) {

        getSp(context).edit().putString(key, value).commit();
    }

    /**
     * 获取字符串
     *
     * @param context 上下文
     * @param key     字符串的键
     * @param value   字符串的默认值
     * @return 得到的字符串
     */
    public static String getString(Context context, String key, String value) {
        return getSp(context).getString(key, value);
    }

    /**
     * 保存布尔值
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void putBoolean(Context context, String key, boolean value) {
        getSp(context).edit().putBoolean(key, value).commit();

    }

    /**
     * 获取布尔值
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 返回保存的值
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSp(context).getBoolean(key, defValue);
    }

    /**
     * 保存long值
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void putLong(Context context, String key, long value) {
        getSp(context).edit().putLong(key, value).commit();
    }

    /**
     * 获取long值
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 保存的值
     */
    public static long getLong(Context context, String key, long defValue) {
        return getSp(context).getLong(key, defValue);
    }

    /**
     * 保存int值
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void putInt(Context context, String key, int value) {

        getSp(context).edit().putInt(key, value).commit();
    }

    /**
     * 获取long值
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 保存的值
     */
    public static int getInt(Context context, String key, int defValue) {
        return getSp(context).getInt(key, defValue);
    }

    /**
     * 保存对象
     *
     * @param context 上下文
     * @param key     键
     * @param obj     要保存的对象（Serializable的子类）
     * @param <T>     泛型定义
     */
    public static <T extends Serializable> void putObject(Context context, String key, T obj) {
        try {
            put(context, key, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象
     *
     * @param context 上下文
     * @param key     键
     * @param <T>     指定泛型
     * @return 泛型对象
     */
    public static <T extends Serializable> T getObject(Context context, String key) {
        try {
            return (T) get(context, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存List
     *
     * @param tag
     * @param datalist
     */
    public static void setDataList(Context context, String tag, List<String> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        getSp(context).edit().putString(tag, strJson).commit();
    }

    /**
     * 获取List
     *
     * @param tag
     * @return
     */
    public static List<String> getDataList(Context context, String tag) {
        List<String> datalist = new ArrayList<>();
        String strJson = getSp(context).getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<String>>() {
        }.getType());
        return datalist;

    }

    public static void setDataListI(Context context, String key,
                                    ArrayList<Integer> ints) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ints.size(); i++) {
            sb.append(ints.get(i) + ":");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        getSp(context).edit().putString(key, sb.toString()).commit();
    }

    public static ArrayList<Integer> getDataListI(Context context, String key) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        String str = getSp(context).getString(key, "");
        if (str.length() > 0) {
            String[] split = str.split(":");

            for (String string : split) {
                result.add(Integer.valueOf(string));
            }
        }
        return result;
    }

    /**
     * 存储Map集合
     *
     * @param key 键
     * @param map 存储的集合
     * @param <K> 指定Map的键
     * @param <T> 指定Map的值
     */

    public <K, T> void setMap(Context context, String key, Map<K, T> map) {
        if (map == null || map.isEmpty() || map.size() < 1) {
            return;
        }

        Gson gson = new Gson();
        String strJson = gson.toJson(map);

        getSp(context).edit().putString(key, strJson).commit();
    }

    /**
     * 获取Map集合
     */
    public <K, T> Map<K, T> getMap(Context context, String key) {
        Map<K, T> map = new HashMap<>();
        String strJson = getSp(context).getString(key, null);
        if (strJson == null) {
            return map;
        }
        Gson gson = new Gson();
        map = gson.fromJson(strJson, new TypeToken<Map<K, T>>() {
        }.getType());
        return map;
    }

    /**
     * 存储对象
     */
    private static void put(Context context, String key, Object obj)
            throws IOException {
        if (obj == null) {//判断对象是否为空
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        // 将对象放到OutputStream中
        // 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        baos.close();
        oos.close();

        putString(context, key, objectStr);
    }

    /**
     * 获取对象
     */
    private static Object get(Context context, String key)
            throws IOException, ClassNotFoundException {
        String wordBase64 = getString(context, key, "");
        // 将base64格式字符串还原成byte数组
        if (TextUtils.isEmpty(wordBase64)) { //不可少，否则在下面会报java.io.StreamCorruptedException
            return null;
        }
        byte[] objBytes = Base64.decode(wordBase64.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        // 将byte数组转换成product对象
        Object obj = ois.readObject();
        bais.close();
        ois.close();
        return obj;
    }
}

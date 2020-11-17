package com.uweic.lib_common.constant;

import android.os.Environment;

public class Constant {


    public static final String SP_NAME_USER = "uweic";//用户相关的SP
    public static final String SP_PERMISSION_LIST = "permissionList";//用户相关的SP
    public static final String SP_TOKEN_EXPIRE_TIME = "tokenExpireTime";//用户相关的SP 过期时间
    public static final String SP_TOKEN = "token";//用户相关的SP
    public static final String SP_UID = "uid";//用户相关的SP
    public static final String SIGN_TOKEN_KEY = "token";//用户相关的SP
    public static final String SIGN_KEY = "youwei-app-sign";//用户相关的SP

    ///文件相关的////
    public static final String STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String MEDIA_DIR = STORAGE_PATH + "/media";
    public static final String RECORD_DIR = STORAGE_PATH + "/record/";
    public static final String RECORD_DOWNLOAD_DIR = STORAGE_PATH + "/record/download/";
    public static final String VIDEO_DOWNLOAD_DIR = STORAGE_PATH + "/video/download/";
    public static final String IMAGE_DOWNLOAD_DIR = STORAGE_PATH + "/image/";
    public static final String FILE_DOWNLOAD_DIR = STORAGE_PATH + "/file/download/";
    public static final String CRASH_LOG_DIR = STORAGE_PATH + "/crash/";


    ///////接口相关的//////
//    public static final String COMMON_REQUEST_PATH = "http://192.168.1.113:8080/Uweic-xinrui/";
    public static final String COMMON_REQUEST_PATH = "http://114.116.245.241:8882/Uweic-xinrui/";
    public static final String COMMON_LIMIT = 10 + "";



}

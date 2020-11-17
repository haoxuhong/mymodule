package com.uweic.lib_dialog;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;
import com.uweic.lib_dialog.base.BaseDialog;
import com.uweic.lib_dialog.widget.NumberProgressBar;

import java.io.File;
import java.util.List;


/**
 * Created by haoxuhong on 2019/9/9.
 *
 * @description: 升级对话框
 */
public final class UpdateDialog {
    public static final class Builder
            extends BaseDialog.Builder<Builder>
            implements View.OnClickListener {

        private TextView mNameView;
        //        private TextView mSizeView;
        private TextView mContentView;
        private NumberProgressBar mProgressView;

        private TextView mUpdateView;
        private AppCompatTextView mCloseView;

        //        * 下载地址
        private String mDownloadUrl;

        //        * 当前下载状态
        private int mDownloadStatus = -1;

        //        * 下载处理对象
        private DownloadHandler mDownloadHandler;
        private OnListener mListener;

        public Builder(FragmentActivity activity) {
            super(activity);

            setContentView(R.layout.dialog_update);
            setAnimStyle(BaseDialog.ANIM_BOTTOM);
            setCancelable(false);

            mNameView = findViewById(R.id.tv_update_name);
//            mSizeView = findViewById(R.id.tv_update_size);
            mContentView = findViewById(R.id.tv_update_content);
            mProgressView = findViewById(R.id.pb_update_progress);

            mUpdateView = findViewById(R.id.tv_update_update);
            mCloseView = findViewById(R.id.tv_update_close);

            mUpdateView.setOnClickListener(this);
            mCloseView.setOnClickListener(this);
        }
        /*
         *
         * 设置版本名*/

        public Builder setVersionName(CharSequence name) {
            mNameView.setText(name);
            return this;
        }

        /*   *//*  *
         * 设置文件大小*//*

        public Builder setFileSize(long size) {
            return setFileSize(Formatter.formatFileSize(getContext(), size));
        }

        public Builder setFileSize(CharSequence text) {
            mSizeView.setText(text);
            return this;
        }*/

        /*  *
         * 设置更新日志*/

        public Builder setUpdateLog(CharSequence text) {
            mContentView.setText(text);
            mContentView.setVisibility(text == null ? View.GONE : View.VISIBLE);
            return this;
        }

        /*  *
         * 设置强制更新*/

        public Builder setForceUpdate(boolean force) {
            mCloseView.setVisibility(force ? View.GONE : View.VISIBLE);
            if (force) {
                setCancelable(true);
            }
            return this;
        }

        /*     *
         * 设置下载 url
         */
        public Builder setDownloadUrl(String url) {
            mDownloadUrl = url;
            return this;
        }

        /* *
         * {@link OnDownloadListener}*/
        public Builder setListener(UpdateDialog.OnListener listener) {
            mListener = listener;
            return this;
        }


        @Override
        public void onClick(View v) {
            if (mListener != null) {
                if (v == mCloseView) {
                    dismiss();
                    mListener.onCancel();
                } else if (v == mUpdateView) {
                    // 判断下载状态
                    switch (mDownloadStatus) {
                        // 没有任何状态
                        case -1:
                            // 下载失败
                        case DownloadManager.STATUS_FAILED:
                            // 重新下载
                            requestPermission();
                            break;
                        // 下载成功
                        case DownloadManager.STATUS_SUCCESSFUL:
                            // 安装 Apk
                            mDownloadHandler.openDownloadFile();
                            break;
                        default:
                            break;
                    }
                }
            }

        }

        /*  *
         * 请求权限*/

        private void requestPermission() {

            PermissionX.init(getActivity())
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)//指定要申请哪些权限
                    .onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {//针对那些被拒绝的权限向用户解释申请的原因并重新申请
                        @Override
                        public void onExplainReason(ExplainScope scope, List<String> deniedList, boolean beforeRequest) {
//                                if (beforeRequest) {
                            scope.showRequestReasonDialog(deniedList, "为了保证程序正常工作，请您同意以下权限申请" + deniedList, "我已明白", "取消");
//                                } else {
//                                    List<String> filteredList = new ArrayList<>();
//                                    for (String permission : deniedList) {
//                                        if (permission.equals(Manifest.permission.CAMERA)) {
//                                            filteredList.add(permission);
//                                        }
//                                    }
//                                    scope.showRequestReasonDialog(filteredList, "摄像机权限是程序必须依赖的权限" + deniedList, "我已明白");
//                                }
                        }
                    })
                    .onForwardToSettings(new ForwardToSettingsCallback() {//针对那些被永久拒绝的权限向用户解释为什么它们是必须的
                        @Override
                        public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                            scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限" + deniedList, "我已明白");
                        }
                    })
                    .request(new RequestCallback() {//开始请求权限，并接收申请的结果
                        @Override
                        public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                            if (allGranted) {
                                downloadApk();
                            } else {
                                Toast.makeText(getActivity(), "您拒绝了如下权限：" + deniedList, Toast.LENGTH_SHORT).show();
                            }
                        }

                        private void downloadApk() {
                            mDownloadHandler = new DownloadHandler(getActivity());
                            mDownloadHandler.setDownloadListener(new OnDownloadListener() {
                                @Override
                                public void downloadProgressChange(int progress) {
                                    mProgressView.setProgress(progress);
                                }

                                @Override
                                public void downloadStateChange(int state) {
                                    // 记录本次的下载状态
                                    mDownloadStatus = state;

                                    // 判断下载状态
                                    switch (state) {
                                        // 下载中
                                        case DownloadManager.STATUS_RUNNING:
                                            mUpdateView.setText(R.string.update_status_running);
                                            // 显示进度条
                                            mProgressView.setVisibility(View.VISIBLE);
                                            break;
                                        // 下载成功
                                        case DownloadManager.STATUS_SUCCESSFUL:
                                            mUpdateView.setText(R.string.update_status_successful);
                                            // 隐藏进度条
                                            mProgressView.setVisibility(View.GONE);
                                            // 安装 Apk
                                            mDownloadHandler.openDownloadFile();
                                            break;
                                        // 下载失败
                                        case DownloadManager.STATUS_FAILED:
                                            mUpdateView.setText(R.string.update_status_failed);
                                            // 删除下载的文件
                                            mDownloadHandler.deleteDownloadFile();
                                            break;
                                        // 下载暂停
                                        case DownloadManager.STATUS_PAUSED:
                                            mUpdateView.setText(R.string.update_status_paused);
                                            break;
                                        // 等待下载
                                        case DownloadManager.STATUS_PENDING:
                                            mUpdateView.setText(R.string.update_status_pending);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            });
//                            TODO     String app_name = getString(R.string.app_name)需要外界传数据进来
                            String app_name = "";
                            if (!mDownloadHandler.createDownload(mDownloadUrl, app_name +
                                    " " + mNameView.getText().toString() + ".apk", null)) {
                                mUpdateView.setText(R.string.update_download_fail);
                            } else {
                                // 设置对话框不能被取消
                                setCancelable(false);
                                // 隐藏取消按钮
                                mCloseView.setVisibility(View.GONE);
                            }
                        }
                    });
        }


    }

    private static final class DownloadHandler extends Handler {

        private final Context mContext;

        //        * 下载管理器对象
        private final DownloadManager mDownloadManager;
        //        * 下载内容观察者
        private DownloadObserver mDownloadObserver;

        //        * 下载文件 id
        private long mDownloadId;

        //        * 下载监听
        private OnDownloadListener mListener;

        //        * 下载的文件
        private File mDownloadFile;

        private DownloadHandler(Context context) {
            super(Looper.getMainLooper());
            mContext = context;
            mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        }

        private void setDownloadListener(OnDownloadListener listener) {
            mListener = listener;
        }

        @Override
        public void handleMessage(Message msg) {
            if (mListener == null) {
                return;
            }

            // 判断下载状态
            switch (msg.what) {
                // 下载中
                case DownloadManager.STATUS_RUNNING:
                    // 计算下载百分比，这里踩了两个坑
                    // 当 apk 文件很大的时候：下载字节数 * 100 会超过 int 最大值，计算结果会变成负数
                    // 还有需要注意的是，int 除以 int 等于 int，这里的下载字节数除以总字节数应该要 double 类型的
                    int progress = (int) (((double) msg.arg2 / msg.arg1) * 100);
                    mListener.downloadProgressChange(progress);
                    break;
                // 下载成功
                case DownloadManager.STATUS_SUCCESSFUL:
                    // 下载失败
                case DownloadManager.STATUS_FAILED:
                    // 移除内容观察者
                    if (mDownloadObserver != null) {
                        mContext.getContentResolver().unregisterContentObserver(mDownloadObserver);
                    }
                    break;
                default:
                    break;
            }

            mListener.downloadStateChange(msg.what);
        }

        /*  *
         * 创建下载任务
         *
         * @param downloadUrl           下载地址
         * @param fileName              文件命名
         * @param notificationTitle     通知栏标题
         * @return                      下载 id*/

        @SuppressWarnings("ResultOfMethodCallIgnored")
        private boolean createDownload(String downloadUrl, String fileName, String notificationTitle) {
            if (fileName == null) {
                throw new IllegalArgumentException("The filename cannot be empty");
            }

            // 记录下载的文件
            mDownloadFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            // 如果这个文件已经下载过，就先删除这个文件
            if (mDownloadFile.exists()) {
                mDownloadFile.delete();
            }

            try {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
                request.allowScanningByMediaScanner();
                //设置WIFI下进行更新
                //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                //设置文件存放目录
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

                if (notificationTitle != null) {
                    request.setTitle(notificationTitle);
                    //设置通知栏的message
//                    request.setDescription("今日头条正在下载.....");

                }

                //进行下载
                mDownloadId = mDownloadManager.enqueue(request);

                mDownloadObserver = new DownloadObserver(this, mDownloadManager, new DownloadManager.Query().setFilterById(mDownloadId));
                // 添加内容观察者
                mContext.getContentResolver().registerContentObserver(Uri.parse("content://downloads/"), true, mDownloadObserver);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        /*        *
         * 打开下载的文件*/

        private void openDownloadFile() {

            PermissionX.init((FragmentActivity) mContext)
                    .permissions(Manifest.permission.REQUEST_INSTALL_PACKAGES)//指定要申请哪些权限
                    .onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {//针对那些被拒绝的权限向用户解释申请的原因并重新申请
                        @Override
                        public void onExplainReason(ExplainScope scope, List<String> deniedList, boolean beforeRequest) {
//                                if (beforeRequest) {
                            scope.showRequestReasonDialog(deniedList, "为了保证程序正常工作，请您同意以下权限申请" + deniedList, "我已明白", "取消");
//                                } else {
//                                    List<String> filteredList = new ArrayList<>();
//                                    for (String permission : deniedList) {
//                                        if (permission.equals(Manifest.permission.CAMERA)) {
//                                            filteredList.add(permission);
//                                        }
//                                    }
//                                    scope.showRequestReasonDialog(filteredList, "摄像机权限是程序必须依赖的权限" + deniedList, "我已明白");
//                                }
                        }
                    })
                    .onForwardToSettings(new ForwardToSettingsCallback() {//针对那些被永久拒绝的权限向用户解释为什么它们是必须的
                        @Override
                        public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                            scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限" + deniedList, "我已明白");
                        }
                    })
                    .request(new RequestCallback() {//开始请求权限，并接收申请的结果
                        @Override
                        public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                            if (allGranted) {
                                installApk();
                            } else {
                                Toast.makeText((FragmentActivity) mContext, "您拒绝了如下权限：" + deniedList, Toast.LENGTH_SHORT).show();
                            }
                        }

                    });



        }

        private void installApk() {
            // 这里需要特别说明的是，这个 API 其实不是打开文件的，我也不知道干什么用的
            // 测试前必须要加权限，否则会崩溃：<uses-permission android:name="android.permission.ACCESS_ALL_DOWNLOADS" />
            // mDownloadManager.openDownloadedFile(mDownloadId);
         /*   if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                //适配Android Q,注意mFilePath是通过ContentResolver得到的，上述有相关代码
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(mFilePath) ,"application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
                return ;
            }*/
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", mDownloadFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                uri = Uri.fromFile(mDownloadFile);
            }

            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }

        /*     *
         * 删除下载的文件*/

        void deleteDownloadFile() {
            mDownloadManager.remove(mDownloadId);
        }
    }

    private static class DownloadObserver extends ContentObserver {

        private final Handler mHandler;
        private final DownloadManager mDownloadManager;
        private final DownloadManager.Query mQuery;

        DownloadObserver(Handler handler, DownloadManager manager, DownloadManager.Query query) {
            super(handler);
            mHandler = handler;
            mDownloadManager = manager;
            mQuery = query;
        }

        /* *
         * 每当 /data/data/com.android.providers.download/database/database.db 变化后就会触发onChange方法
         *
         * @param selfChange     是否是当前应用自己操作了数据库
         */
        @Override
        public void onChange(boolean selfChange) {
            // 查询数据库
            Cursor cursor = mDownloadManager.query(mQuery);
            // 游标定位到第一个，因为 Cursor 总数只有一个
            cursor.moveToFirst();

            // 总需下载的字节数
            int totalBytes = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            // 已经下载的字节数
            int downloadBytes = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            // 下载状态
            int downloadStatus = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));

            // 关闭游标
            cursor.close();

            // 发送更新消息
            Message msg = mHandler.obtainMessage();
            msg.arg1 = totalBytes;
            msg.arg2 = downloadBytes;
            msg.what = downloadStatus;
            mHandler.sendMessage(msg);
        }
    }

    private interface OnDownloadListener {
        /* *
         * 下载进度改变*/

        void downloadProgressChange(int progress);

        /**
         * 下载状态改变
         */

        void downloadStateChange(int state);
    }

    public interface OnListener {
        /*   *
         * 点击取消时回调*/

        void onCancel();
    }
}
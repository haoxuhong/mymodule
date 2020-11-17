package com.uweic.mymodule.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.uweic.lib_dialog.base.BaseDialog;
import com.uweic.mymodule.R;

public class CustomParkingDialog {
    public static final class Builder
            extends BaseDialog.Builder<Builder> {
        private OnListener mListener;
        private final AppCompatButton confirmBtn;
        private final AppCompatTextView tv2;
        private final AppCompatTextView tv5;
        private final AppCompatImageView closeIv;

        public Builder(Context context) {
            super(context);

            setContentView(R.layout.dialog_custom_parking);
            setAnimStyle(BaseDialog.ANIM_BOTTOM);
            setGravity(Gravity.BOTTOM);

            tv2 = findViewById(R.id.dialog_custom_parking_tv2);
            tv5 = findViewById(R.id.dialog_custom_parking_tv5);
            closeIv = findViewById(R.id.dialog_custom_parking_close_iv);
            confirmBtn = findViewById(R.id.dialog_custom_parking_confirm_btn);
            setOnClickListener(closeIv, confirmBtn);

        }

        public Builder setParkingType(boolean type) {

            tv2.setText(type ? "落锁" : "上锁");
            tv5.setText(type ? "上锁" : "落锁");
            return this;
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_custom_parking_confirm_btn:
                    dismiss();
                    if (mListener != null) {
                        mListener.onConfirm(getDialog());
                    }
                    break;
                case R.id.dialog_custom_parking_close_iv:
                    dismiss();
                    if (mListener != null) {
                        mListener.onCancel(getDialog());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public interface OnListener {

        /**
         * 点击确定时回调
         */
        void onConfirm(BaseDialog dialog);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {
        }
    }
}

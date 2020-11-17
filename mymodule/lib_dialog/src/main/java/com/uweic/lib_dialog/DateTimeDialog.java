package com.uweic.lib_dialog;


import android.view.View;

import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;

import com.uweic.lib_dialog.base.BaseDialog;
import com.uweic.lib_dialog.widget.LoopView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by haoxuhong on 2019/10/22.
 *
 * @description: 日期时间对话框
 * 一月、三月、五月、七月、八月、十月、十二月为31天
 * <p>
 * 二月非闰年为28天，在闰年为29天，闰年指年份能被4整除的年，如2000年能被4整除，为闰年
 * <p>
 * 四月、六月、九月、十一月为30天
 */

public final class DateTimeDialog {
    public static final class Builder
            extends BaseDialog.Builder<Builder>
            implements LoopView.LoopScrollListener,
            View.OnClickListener {

        private final int mStartYear = 1920;
        private final int mEndYear = 2038;

        private final AppCompatTextView mTitleView, mCancelView, mConfirmView;
        private final LoopView mHourView, mSecondView, mMinuteView, mYearView, mMonthView, mDayView;

        private DateTimeDialog.OnListener mListener;
        private boolean mAutoDismiss = true;

        @SuppressWarnings("all")
        public Builder(FragmentActivity activity) {
            super(activity);
            setContentView(R.layout.dialog_date_time);

            mTitleView = findViewById(R.id.tv_date_time_title);
            mCancelView = findViewById(R.id.tv_date_time_cancel);
            mConfirmView = findViewById(R.id.tv_date_time_confirm);

            mYearView = findViewById(R.id.lv_date_time_year);
            mMonthView = findViewById(R.id.lv_date_time_month);
            mDayView = findViewById(R.id.lv_date_time_day);

            mHourView = findViewById(R.id.lv_date_time_hour);
            mMinuteView = findViewById(R.id.lv_date_time_minute);
            mSecondView = findViewById(R.id.lv_date_time_second);

            // 生产年份
            ArrayList<String> yearData = new ArrayList<>(10);
            for (int i = mStartYear; i <= mEndYear; i++) {
                yearData.add(i + " " + getString(R.string.common_year));
            }

            // 生产月份
            ArrayList<String> monthData = new ArrayList<>(12);
            for (int i = 1; i <= 12; i++) {
                monthData.add((i < 10 ? "0" : "") + i + " " + getString(R.string.common_month));
            }
            // 生产小时
            ArrayList<String> hourData = new ArrayList<>(24);
            for (int i = 0; i <= 23; i++) {
                hourData.add((i < 10 ? "0" : "") + i + " " + getString(R.string.common_hour));
            }

            // 生产分钟
            ArrayList<String> minuteData = new ArrayList<>(60);
            for (int i = 0; i <= 59; i++) {
                minuteData.add((i < 10 ? "0" : "") + i + " " + getString(R.string.common_minute));
            }

            // 生产秒钟
            ArrayList<String> secondData = new ArrayList<>(60);
            for (int i = 0; i <= 59; i++) {
                secondData.add((i < 10 ? "0" : "") + i + " " + getString(R.string.common_second));
            }

            mYearView.setData(yearData);
            mMonthView.setData(monthData);
            mHourView.setData(hourData);
            mMinuteView.setData(minuteData);
            mSecondView.setData(secondData);

            mYearView.setLoopListener(this);
            mMonthView.setLoopListener(this);
            mDayView.setLoopListener(this);

            mCancelView.setOnClickListener(this);
            mConfirmView.setOnClickListener(this);

            Calendar calendar = Calendar.getInstance();
            setYear(calendar.get(Calendar.YEAR));
            setMonth(calendar.get(Calendar.MONTH) + 1);
            setDay(calendar.get(Calendar.DAY_OF_MONTH));
            setHour(calendar.get(Calendar.HOUR_OF_DAY));
            setMinute(calendar.get(Calendar.MINUTE));
            setSecond(calendar.get(Calendar.SECOND));
        }

        public DateTimeDialog.Builder setTitle(@StringRes int id) {
            return setTitle(getString(id));
        }

        public DateTimeDialog.Builder setTitle(CharSequence text) {
            mTitleView.setText(text);
            return this;
        }

        public DateTimeDialog.Builder setCancel(@StringRes int id) {
            return setCancel(getString(id));
        }

        public DateTimeDialog.Builder setCancel(CharSequence text) {
            mCancelView.setText(text);
            return this;
        }

        public DateTimeDialog.Builder setConfirm(@StringRes int id) {
            return setConfirm(getString(id));
        }

        public DateTimeDialog.Builder setConfirm(CharSequence text) {
            mConfirmView.setText(text);
            return this;
        }

        public DateTimeDialog.Builder setListener(DateTimeDialog.OnListener listener) {
            mListener = listener;
            return this;
        }

        public DateTimeDialog.Builder setAutoDismiss(boolean dismiss) {
            mAutoDismiss = dismiss;
            return this;
        }

        /**
         * 不选择天数
         */

        public DateTimeDialog.Builder setIgnoreDay() {
            mDayView.setVisibility(View.GONE);
            return this;
        }

        public DateTimeDialog.Builder setDate(long date) {
            if (date > 0) {
                setDate(new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date(date)));
            }
            return this;
        }

        public DateTimeDialog.Builder setDate(String date) {
            // 20190519
            if (date.matches("\\d{8}")) {
                setYear(date.substring(0, 4));
                setMonth(date.substring(4, 6));
                setDay(date.substring(6, 8));
                // 2019-05-19
            } else if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                setYear(date.substring(0, 4));
                setMonth(date.substring(5, 7));
                setDay(date.substring(8, 10));
            }
            return this;
        }

        public DateTimeDialog.Builder setYear(String year) {
            return setYear(Integer.valueOf(year));
        }

        public DateTimeDialog.Builder setYear(int year) {
            int index = year - mStartYear;
            if (index < 0) {
                index = 0;
            } else if (index > mYearView.getSize() - 1) {
                index = mYearView.getSize() - 1;
            }
            mYearView.setInitPosition(index);
            return this;
        }

        public DateTimeDialog.Builder setMonth(String month) {
            return setMonth(Integer.valueOf(month));
        }

        public DateTimeDialog.Builder setMonth(int month) {
            int index = month - 1;
            if (index < 0) {
                index = 0;
            } else if (index > mMonthView.getSize() - 1) {
                index = mMonthView.getSize() - 1;
            }
            mMonthView.setInitPosition(index);
            return this;
        }

        public DateTimeDialog.Builder setDay(String day) {
            return setDay(Integer.valueOf(day));
        }

        public DateTimeDialog.Builder setDay(int day) {
            int index = day - 1;
            if (index < 0) {
                index = 0;
            } else if (index > mDayView.getSize() - 1) {
                index = mDayView.getSize() - 1;
            }
            mDayView.setInitPosition(index);
            return this;
        }

        public DateTimeDialog.Builder setTime(String time) {
            // 102030
            if (time.matches("\\d{6}")) {
                setHour(time.substring(0, 2));
                setMinute(time.substring(2, 4));
                setSecond(time.substring(4, 6));
                // 10:20:30
            } else if (time.matches("\\d{2}:\\d{2}:\\d{2}")) {
                setHour(time.substring(0, 2));
                setMinute(time.substring(3, 5));
                setSecond(time.substring(6, 8));
            }
            return this;
        }

        public DateTimeDialog.Builder setHour(String hour) {
            return setHour(Integer.valueOf(hour));
        }

        public DateTimeDialog.Builder setHour(int hour) {
            int index = hour;
            if (index < 0 || hour == 24) {
                index = 0;
            } else if (index > mHourView.getSize() - 1) {
                index = mHourView.getSize() - 1;
            }
            mHourView.setInitPosition(index);
            return this;
        }

        public DateTimeDialog.Builder setMinute(String minute) {
            return setMinute(Integer.valueOf(minute));
        }

        public DateTimeDialog.Builder setMinute(int minute) {
            int index = minute;
            if (index < 0) {
                index = 0;
            } else if (index > mMinuteView.getSize() - 1) {
                index = mMinuteView.getSize() - 1;
            }
            mMinuteView.setInitPosition(index);
            return this;
        }

        public DateTimeDialog.Builder setSecond(String second) {
            return setSecond(Integer.valueOf(second));
        }

        public DateTimeDialog.Builder setSecond(int second) {
            int index = second;
            if (index < 0) {
                index = 0;
            } else if (index > mSecondView.getSize() - 1) {
                index = mSecondView.getSize() - 1;
            }
            mSecondView.setInitPosition(index);
            return this;
        }

        @Override
        public void onItemSelect(LoopView loopView, int position) {
            // 获取这个月最多有多少天
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            if (loopView == mYearView) {
                calendar.set(mStartYear + mYearView.getSelectedItem(), mMonthView.getSelectedItem(), 1);
            } else if (loopView == mMonthView) {
                calendar.set(mStartYear + mYearView.getSelectedItem(), mMonthView.getSelectedItem(), 1);
            } else if (loopView == mDayView) {
                calendar.set(mStartYear + mYearView.getSelectedItem(), mMonthView.getSelectedItem(), 1);
            }

            int day = calendar.getActualMaximum(Calendar.DATE);

            ArrayList<String> dayData = new ArrayList<>(day);
            for (int i = 1; i <= day; i++) {
                dayData.add((i < 10 ? "0" : "") + i + " " + getString(R.string.common_day));
            }

            mDayView.setData(dayData);
        }

        @Override
        public void onClick(View v) {
            if (mAutoDismiss) {
                dismiss();
            }

            if (mListener != null) {
                if (v == mConfirmView) {

                    mListener.onSelected(getDialog(), mStartYear + mYearView.getSelectedItem(), mMonthView.getSelectedItem() + 1, mDayView.getSelectedItem() + 1, mHourView.getSelectedItem(), mMinuteView.getSelectedItem(), mSecondView.getSelectedItem());
                } else if (v == mCancelView) {
                    mListener.onCancel(getDialog());
                }
            }
        }
    }

    public interface OnListener {

        /**
         * 选择完日期后回调
         *
         * @param year  年
         * @param month 月
         * @param day   日
         */

        void onSelected(BaseDialog dialog, int year, int month, int day, int hour, int minute, int second);

        /**
         * 点击取消时回调
         */

        void onCancel(BaseDialog dialog);
    }
}

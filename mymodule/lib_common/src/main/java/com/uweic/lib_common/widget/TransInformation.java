package com.uweic.lib_common.widget;

import android.text.method.ReplacementTransformationMethod;

/**
 * Created by haoxuhong on 2020/12/14.
 *
 * @description: android EditText 输入字母时小写自动转为大写
 * 使用：mBinding.whitelistPlateNumberCet.setTransformationMethod(new TransInformation());
 * 注意取值时，需要将取到的EditText的值利用.trim().toUpperCase()转换成大写。如：String plateNumber = mBinding.whitelistPlateNumberCet.getText().toString().trim().toUpperCase();
 */
public class TransInformation extends ReplacementTransformationMethod {
    @Override
    protected char[] getOriginal() {
        char[] originalCharArr = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        return originalCharArr;
    }

    @Override
    protected char[] getReplacement() {
        char[] replacementCharArr = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        return replacementCharArr;
    }
}

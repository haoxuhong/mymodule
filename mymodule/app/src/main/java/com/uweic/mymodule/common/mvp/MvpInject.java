package com.uweic.mymodule.common.mvp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by haoxuhong on 2019/12/23.
 *
 * @description: Mvp 实例化注解
 */
@Target(ElementType.FIELD) // 字段注解
@Retention(RetentionPolicy.RUNTIME) // 运行时注解
public @interface MvpInject {
}
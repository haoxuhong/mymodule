package com.uweic.lib_base.autoservice;

import java.util.ServiceLoader;

/**
 * Created by haoxuhong on 2020/8/20
 *
 * @description:
 */
public final class MyServiceLoader {

    private MyServiceLoader() {
    }

    public static <S> S load(Class<S> service) {

        try {
            return ServiceLoader.load(service).iterator().next();
        } catch (Exception e) {
            return null;
        }
    }
}

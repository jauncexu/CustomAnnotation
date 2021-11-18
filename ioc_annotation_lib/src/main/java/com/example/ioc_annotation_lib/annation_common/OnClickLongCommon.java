package com.example.ioc_annotation_lib.annation_common;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 长按的注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@OnBaseCommon(setCommonListener = "setOnLongClickListener",
        setCommonObjectListener = View.OnLongClickListener.class,  // 第2个要素
        callbackMethod = "onLongClick") // 如何动态监听此 onLongClick函数的执行
public @interface OnClickLongCommon {
    int value();
}

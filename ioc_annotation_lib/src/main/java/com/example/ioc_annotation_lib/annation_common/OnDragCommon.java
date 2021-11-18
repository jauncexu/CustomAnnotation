package com.example.ioc_annotation_lib.annation_common;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@OnBaseCommon(setCommonListener = "setOnDragListener", // 第1个要素
             setCommonObjectListener = View.OnDragListener.class, // 第2个要素
             callbackMethod = "onDrag") // 第3个要素
public @interface OnDragCommon {
    int value();
}

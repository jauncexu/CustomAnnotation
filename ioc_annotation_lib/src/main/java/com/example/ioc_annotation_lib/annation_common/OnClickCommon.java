package com.example.ioc_annotation_lib.annation_common;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 点击的注解
@Target(ElementType.METHOD) // 作用域方法
@Retention(RetentionPolicy.RUNTIME) // 运行时
@OnBaseCommon(
        setCommonListener = "setOnClickListener",
        setCommonObjectListener = View.OnClickListener.class,  // 第2个要素
        callbackMethod = "onClick"  // 如何动态监听此 onClick函数的执行
)// 子注解已经给 父注解赋值了
public @interface OnClickCommon {
    int value() default -1; // 你的控件 就是 int id值  R.id.bt_t1
}

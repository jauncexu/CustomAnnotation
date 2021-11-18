package com.example.ioc_annotation_lib.annation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidx.core.app.RemoteInput;

@Target(ElementType.TYPE)  // 注解作用在类上
@Retention(RetentionPolicy.RUNTIME) // 运行时期
public @interface ContentView {
    int value() default -1; // 对应布局的id
}

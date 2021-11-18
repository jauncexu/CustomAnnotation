package com.example.ioc_annotation_lib.annation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 作用在方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface OnClick {
    int value() default -1; // 你的控件 就是 int id值
}

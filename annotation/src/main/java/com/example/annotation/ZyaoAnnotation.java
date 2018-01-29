package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ZTH on 2018/1/22.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ZyaoAnnotation {

    String name() default "undefined";
    String text() default "";

}

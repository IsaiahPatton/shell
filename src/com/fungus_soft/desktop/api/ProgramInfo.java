package com.fungus_soft.desktop.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE)
public @interface ProgramInfo {

    public String name() default "Untitled Program";
    public String version() default "Unknown";
    public String authors() default "Unknown";
    public String minShellVersion() default "_all_";

    public int width() default 600;
    public int height() default 300;

}
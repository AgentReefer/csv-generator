package com.reportgenerator.annotations;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CsvColumn {
    String name();
}


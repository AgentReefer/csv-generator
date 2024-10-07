package com.reportgenerator.Utils;



import com.reportgenerator.annotations.CsvColumn;
import com.reportgenerator.exceptions.NoCsvColumnsException;

import java.lang.reflect.Field;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReflectionUtils {

    private static final Logger logger = Logger.getLogger(ReflectionUtils.class.getName());
    public static boolean hasCsvColumns(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(CsvColumn.class)) {
                return true;
            }
        }
        return false;
    }

    public static String getCsvHeader(Class<?> clazz) {
        StringJoiner header = new StringJoiner(",");
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(CsvColumn.class)) {
                CsvColumn annotation = field.getAnnotation(CsvColumn.class);
                header.add(annotation.name());
            }
        }
        return header.toString();
    }

    public static String getCsvRow(Object object) throws NoCsvColumnsException, IllegalAccessException {
        StringJoiner row = new StringJoiner(",");
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(CsvColumn.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(object);
                    row.add(value != null ? value.toString() : "");
                } catch (IllegalAccessException e) {
                    logger.log(Level.SEVERE, "Error accessing field: " + field.getName(), e);
                    throw e;
                }
            }
        }
        return row.toString();
    }
}

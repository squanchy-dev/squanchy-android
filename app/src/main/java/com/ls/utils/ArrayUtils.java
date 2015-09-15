package com.ls.utils;

public class ArrayUtils {

    public static String[] build(Object... values) {
        String[] arr = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            arr[i] = String.valueOf(values[i]);
        }
        return arr;
    }
}

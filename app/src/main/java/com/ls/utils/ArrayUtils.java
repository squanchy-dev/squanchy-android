package com.ls.utils;

/**
 * Created by Yakiv M. on 23.09.2014.
 */
public class ArrayUtils {

    public static String[] build(Object... values) {
        String[] arr = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            arr[i] = String.valueOf(values[i]);
        }
        return arr;
    }
}

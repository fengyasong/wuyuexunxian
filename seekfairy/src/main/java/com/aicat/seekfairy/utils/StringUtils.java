package com.aicat.seekfairy.utils;

import java.util.Arrays;

public class StringUtils {
    /**
     * 字符串转换为整型数组
     * @param param
     * @return
     */
    /*public static Integer[] toIntegerArray(String param) {
        return Arrays.stream(param.split(","))
                .map(Integer::valueOf)
                .toArray(Integer[]::new);
    }*/
    public static Integer[] toIntegerArray(String... param) {
        return Arrays.stream(param).map(Integer::valueOf).toArray(Integer[]::new);
    }
    public static void main(String[] args) {
        Integer[] a=toIntegerArray("1");
        System.out.println(Arrays.stream(a));
    }

}

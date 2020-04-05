package com.aicat.seekfairy.utils;

import java.io.File;
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
        //Integer[] a=toIntegerArray("1");
        File file = new File("D:\\IdeaProjects\\0722\\ttt2\\tttt\\.idea");
        File [] extractedFiles = file.listFiles();

        Arrays.stream(extractedFiles).forEach(e->FileUtils.copyFile(e.getPath(),"D:\\IdeaProjects\\0722\\temp\\"+e.getName()));
        Arrays.stream(extractedFiles).forEach(e->FileUtils.copyFile(e.getAbsolutePath(),"D:\\IdeaProjects\\0722\\temp2\\"+e.getName()));
        //System.out.println(Arrays.stream(a));
    }

}

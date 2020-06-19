package com.aicat.common.utils;

import java.util.*;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * 字符串转换为整型数组
     * @param param
     * @return
     */
    public static Integer[] toIntegerArray(String param) {
        return Arrays.stream(param.split(","))
                .map(Integer::valueOf)
                .toArray(Integer[]::new);
    }
    public static Integer[] toIntegerArray(String[] param) {
        return Arrays.stream(param).map(Integer::valueOf).toArray(Integer[]::new);
    }
    public static void main(String[] args) {
        /*HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            map.put(Integer.toString(i), String.valueOf(i));
        }
        long time1 = System.currentTimeMillis();
        Set<Map.Entry<String, String>> set = map.entrySet();
        Iterator<Map.Entry<String, String>> iter = set.iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
        long time2 = System.currentTimeMillis();
        map.forEach((k, v) -> System.out.println(k + "=" + v));
        long time3 = System.currentTimeMillis();
        System.out.println("方法一耗时=" + (time2 - time1));
        System.out.println("方法二耗时=" + (time3 - time2));*/
        //Integer[] a = Arrays.stream(",,,".split(",")).map(Integer::valueOf).toArray(Integer[]::new);
        String[] a = "1,2,3,4".split(",");
        System.out.println(a);
        List<String> list = Arrays.asList(a);//此list不可以操作
        list.add("6");//报错
        List<String> list2 = new ArrayList<>();
        Collections.addAll(list2,a);
        list2.add("5");
        System.out.println(list2);

        //System.out.println(Arrays.stream(a));
        /*File file = new File("D:\\IdeaProjects\\0722\\ttt2\\tttt\\.idea");
        File [] extractedFiles = file.listFiles();

        Arrays.stream(extractedFiles).forEach(e->FileUtils.copyFile(e.getPath(),"D:\\IdeaProjects\\0722\\temp\\"+e.getName()));
        Arrays.stream(extractedFiles).forEach(e->FileUtils.copyFile(e.getAbsolutePath(),"D:\\IdeaProjects\\0722\\temp2\\"+e.getName()));*/
    }

}

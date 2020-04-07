package com.aicat.common.utils;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class JsonUtils {
    /**
     * json 转lsit
     *
     * @param json
     * @param c
     * @return
     */
    public static <T> List<T> toList(String json, Class<T> c) {
        JSONArray jsonArray = JSONUtil.parseArray(json);
        return JSONUtil.toList(jsonArray, c);
    }

    /**
     * 本方法封装了往前台设置的header,contentType等信息
     */
    public static void writeToWeb(String message, HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/html; charset=utf-8");
        try {
            response.getWriter().write(message);
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

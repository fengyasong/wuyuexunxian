package com.aicat.seekfairy.controller;

import com.aicat.common.utils.R;
import com.aicat.seekfairy.entity.Food;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("test")
public class Test {

    @PostMapping("liu")
    R add(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 请求消息类型
        String contentType = request.getContentType();
        int totalbytes = request.getContentLength();

        /*Map map = new HashMap();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();

            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }*/

        /*Map map = request.getParameterMap();
        String text = "";
        if (map != null) {
            Set set = map.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                if (entry.getValue() instanceof String[]) {
                    log.info("==A==entry的key： " + entry.getKey());
                    String key = (String) entry.getKey();
                    if (key != null && !"id".equals(key) && key.startsWith("[") && key.endsWith("]")) {
                        text = (String) entry.getKey();
                        break;
                    }
                    String[] values = (String[]) entry.getValue();
                    for (int i = 0; i < values.length; i++) {
                        log.info("==B==entry的value: " + values[i]);
                        key += "="+values[i];
                    }
                    if (key.startsWith("[") && key.endsWith("]")) {
                        text = (String) entry.getKey();
                        break;
                    }
                } else if (entry.getValue() instanceof String) {
                    log.info("==========entry的key： " + entry.getKey());
                    log.info("==========entry的value: " + entry.getValue());
                }
            }
        }*/

        //String reqcontent = JSONUtils.toJSONString(map);
        // 容纳请求消息实体的字节数组
        /*byte[] dataOrigin = new byte[totalbytes];
        // 得到请求消息的数据输入流
        InputStream inputStream = request.getInputStream();
        DataInputStream in = new DataInputStream(inputStream);
        in.readFully(dataOrigin); // 根据长度，将消息实体的内容读入字节数组dataOrigin中
        in.close(); // 关闭数据流
        String reqcontent = new String(dataOrigin); // 从字节数组中得到表示实体的字符串*/
        // 从字符串中得到输出缓冲流
        /*BufferedReader reqbuf = new BufferedReader(new StringReader(reqcontent));
        // 设置循环标志
        boolean flag = true;
        while (flag == true) {
            String s = reqbuf.readLine();
            if(s==null) flag=false;
        }*/
        InputStream inputStream = request.getInputStream();
        byte buffer[] = new byte[1024];
        // 定义一个StringBuffer用来存放字符串
        StringBuffer sb = new StringBuffer();
        int len;
        while ((len = inputStream.read(buffer,0,1024))!=-1){
            sb.append(new String(buffer,0,len));
        }
        return R.ok().put("reqcontent",sb.toString());
    }

    public static void main(String[] args) {
        List<Food> list = new ArrayList<>(10);
        Food food;
        for (int i = 0; i < 10; i++) {
            food = new Food();
            food.setId((long) i);
            food.setName("name" + i);
            list.add(food);
        }
        System.out.println(list.toString());
    }


}

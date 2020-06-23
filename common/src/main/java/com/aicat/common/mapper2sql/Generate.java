package com.aicat.common.mapper2sql;

import com.aicat.common.utils.FileUtils;
import com.aicat.common.utils.StringUtils;
import com.aicat.common.utils.XmlParser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Generate {
    /**
     * def create_mapper(xml=None, xml_raw_text=None):
     *     Parse XML files
     *     Get mybatis mapper
     */

    public static Map<String,String> create_mapper(String xmlPath,String xml_raw_text){
        if(StringUtils.isBlank(xml_raw_text)){
            xml_raw_text = FileUtils.readFile(xmlPath);
        }
        xml_raw_text = replace_cdata(xml_raw_text);
        Map<String,String> statement = new HashMap<>();
        Map<String,Element> map= new HashMap<>();
        try {
            Document doc = DocumentHelper.parseText(xml_raw_text);
            Element root = doc.getRootElement();
            String namespace = root.attributeValue("namespace");
            if(StringUtils.isBlank(namespace)){
                return statement;
            }
            List<Element> list = XmlParser.getChildList(root);
            list.forEach(e->{
                if(Convert.query_types.contains(e.getName())){
                    map.put(e.attributeValue("id"),e);
                }
            });
            statement = get_statement(map,namespace);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return statement;
    }
    public static Map<String,String> get_statement(Map<String,Element> mybatis_mapper,String namespace) {
        Map<String,String> statement = new HashMap<>();
        mybatis_mapper.forEach((child_id, child)->{
            if(!"sql".equalsIgnoreCase(child.getName())){
                //Map<String,String> child_statement = new HashMap<>();
                statement.put(namespace+"."+child_id, get_child_statement(mybatis_mapper, child_id));
                //child_statement.put(child_id, get_child_statement(mybatis_mapper, child_id));
                //statement.putAll(child_statement);
            }
        });
        return statement;
    }
    public static String get_child_statement(Map<String,Element> mybatis_mapper, String child_id) {
        StringBuffer statement = new StringBuffer();
        Element child = mybatis_mapper.get(child_id);

        Map<String,Object> kwargs = new HashMap<>();
        statement.append(Convert.convert_children(mybatis_mapper, child,kwargs));
        //The child element has children
        List<Element> list = XmlParser.getChildList(child);
        list.forEach(next_child->
                statement.append(Convert.convert_children(mybatis_mapper, next_child,kwargs)));
        return statement.toString();
    }

    public static String replace_cdata(String raw_text) {
        String cdata_regex = "(<!\\[CDATA\\[)([\\s\\S]*?)(\\]\\]>)";
        ///////cdata_regex = '(<!\[CDATA\[)([\s\S]*?)(\]\]>)'
        ///////cdata_regex = '(<![CDATA[)([\s\S]*?)(]]>)'
        Pattern pattern = Pattern.compile(cdata_regex);
        Matcher match = pattern.matcher(raw_text);
        if(match.find()){
            String cdata_text = match.group(2);
            cdata_text = Convert.convert_cdata(cdata_text, true);
            raw_text = raw_text.replace(match.group(), cdata_text);
        }
        return raw_text;
    }

    public static void main(String[] args) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
                "<mapper namespace=\"Test\">\n" +
                "    <sql id=\"sometable\">\n" +
                "        fruits\n" +
                "    </sql>\n" +
                "    <sql id=\"somewhere\">\n" +
                "        WHERE\n" +
                "        category = #{category}\n" +
                "    </sql>\n" +
                "    <sql id=\"someinclude\">\n" +
                "        FROM\n" +
                "        <include refid=\"${include_target}\"/>\n" +
                "        <include refid=\"somewhere\"/>\n" +
                "    </sql>\n" +
                "    <select id=\"testForeach\">\n" +
                "        SELECT\n" +
                "        name,\n" +
                "        category,\n" +
                "" +
                "" +
                "" +
                "" +
                "" +

                "        price\n" +
                "        FROM\n" +
                "        fruits\n" +
                "        <where>\n" +
                "" +
                "" +
                "            category = 'apple' AND\n" +
                "" +
                "" +
                "" +
                "<if test=\"age lt 18\">" +
                "<![CDATA[  age<#{age} and ]]>" +
                "</if>"+
                "            <foreach collection=\"apples\" item=\"name\" open=\"(\" close=\")\" separator=\"OR\">\n" +
                "                <if test=\"name == 'Jonathan' or name == 'Fuji'\">\n" +
                "                    name = #{name}\n" +
                "                </if>\n" +
                "            </foreach>\n" +
                "        </where>\n" +
                "order by id desc"+
                "    </select>\n" +
                "</mapper>";
        long start = System.currentTimeMillis();
        Map<String,String> statement = create_mapper(null,xml);
        long end = System.currentTimeMillis();
        //Map<String,String> statement = get_statement(mapper);
        statement.forEach((k,v)->System.out.println(k+"==="+v));
        System.out.println(end-start);

    }
}
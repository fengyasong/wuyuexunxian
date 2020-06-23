package com.aicat.common.mapper2sql;

import com.aicat.common.utils.XmlParser;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Convert {

    public static List<String> query_types = new ArrayList<>();

    static {
        query_types.add("sql");
        query_types.add("select");
        query_types.add("insert");
        query_types.add("update");
        query_types.add("delete");
    }
    public static String  convert_children(Map<String, Element> mybatis_mapper, Element child,Map<String,Object> kwargs) {
        if (query_types.contains(child.getName())){
            return convert_parameters(child,true,true);
        }else if("include".equalsIgnoreCase(child.getName())){
            return convert_include(mybatis_mapper, child,kwargs);
        }else if("if".equalsIgnoreCase(child.getName())){
            return convert_if(mybatis_mapper,child,kwargs);
        }else if("choose".equalsIgnoreCase(child.getName())||"when".equalsIgnoreCase(child.getName())||"otherwise".equalsIgnoreCase(child.getName())){
            return convert_choose_when_otherwise(mybatis_mapper, child, kwargs);
        }else if("trim".equalsIgnoreCase(child.getName())||"where".equalsIgnoreCase(child.getName())||"set".equalsIgnoreCase(child.getName())){
            return convert_trim_where_set(mybatis_mapper, child, kwargs);
        }else if("foreach".equalsIgnoreCase(child.getName())){
            return convert_foreach(mybatis_mapper, child, kwargs);
        }else if("bind".equalsIgnoreCase(child.getName())){
            return convert_bind(child, kwargs);
        }else {
            return "";
        }
    }

    /**
     * Replace CDATA String
     *     :param string:
     *     :param reverse = false
     * @return
     */
    public static String convert_cdata(String string, boolean reverse) {
        if (reverse) {
            string = string.replaceAll("&", "&amp;")
                    .replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;")
                    .replaceAll("\"", "&quot;");
        } else {
            string = string.replaceAll("&amp;", "&")
                    .replaceAll("&lt;", "<")
                    .replaceAll("&gt;", ">")
                    .replaceAll("&quot;", "\"");
        }
        return string;
    }

    /**
     * text=False, tail=False
     * @param child
     * @param text
     * @param tail
     * @return
     */
    public static String convert_parameters(Element child,boolean text, boolean tail) {
        Pattern pattern = Pattern.compile("\\S");
        // Remove empty info
        String child_text = XmlParser.getText(child);
        String child_tail = XmlParser.getTail(child);
        Matcher match = pattern.matcher(child_text);
        child_text = match.find() ? child_text : "";
        match = pattern.matcher(child_tail);
        child_tail = match.find() ? child_tail : "";
        String convert_string;
        if (text && tail) {
            convert_string = child_text + child_tail;
        } else if (text && !tail) {
            convert_string = child_text;
        } else if (!text && tail) {
            convert_string = child_tail;
        } else {
            convert_string = "";
        }
        //# replace params
        Map<String, List<Map<String, String>>> params = Params.get_params(child);
        List<Map<String, String>> paramsAll = params.get("#");
        paramsAll.addAll(params.get("$"));
        for (Map<String, String> param : paramsAll) {
            convert_string = convert_string.replace(param.get("full_name"), param.get("mock_value"));
        }
        // convert CDATA string
        convert_string = convert_cdata(convert_string, false);
        return convert_string;
    }
    public static String convert_include(Map<String,Element> mybatis_mapper,Element child,Map<String,Object> kwargs){
        // Add Properties
        Map<String,String> properties = kwargs.get("properties") == null ? new HashMap<>() : (Map<String,String>)kwargs.get("properties");
        List<Element> list = XmlParser.getChildList(child);
        list.forEach(next_child->{
            if ("property".equalsIgnoreCase(next_child.getName())){
                properties.put(next_child.attributeValue("name"),next_child.attributeValue("value"));
            }
        });
        String convert_string = " ";
        String include_child_id = child.attributeValue("refid");
        Pattern pattern ;
        Matcher match ;
        String [] symbols = {"#","$"};
        for (String change:symbols) {
            String string_regex = "\\" + change + "\\{.+?\\}";
            pattern = Pattern.compile(string_regex);
            match = pattern.matcher(include_child_id) ;
            if(match.find()){
                include_child_id = include_child_id.replace(change + "{", "").replace("}", "");
                include_child_id = properties.get(include_child_id);
                break;
            }
        }
        Element include_child = mybatis_mapper.get(include_child_id);
        List<Element> include_child_list = XmlParser.getChildList(include_child);
        convert_string += convert_children(mybatis_mapper, include_child,kwargs);
        // add include text
        convert_string += convert_parameters(child,true,false);
        for (Element next_child:include_child_list) {
            kwargs.put("properties",properties);
            convert_string += convert_children(mybatis_mapper, next_child, kwargs);
        }
        //# add include tail
        convert_string += convert_parameters(child, false,true);
        return convert_string;
    }

    public static String convert_if(Map<String,Element> mybatis_mapper,Element child,Map<String,Object> kwargs){
        String convert_string = " ";
        String test = child.attributeValue("test");
        //# Add if text
        convert_string += convert_parameters(child,true,false);
        List<Element> list = XmlParser.getChildList(child);
        for (Element next_child:list) {
            convert_string += convert_children(mybatis_mapper, next_child, kwargs);
        }
        //convert_string += "-- if(" + test + ")\n";
        //# Add if tail
        convert_string += convert_parameters(child,false, true);
        return convert_string;
    }

    public static String convert_choose_when_otherwise(Map<String,Element> mybatis_mapper,Element child,Map<String,Object> kwargs){
        // native
        String nativeValue = kwargs.get("native")==null?null:kwargs.get("native").toString();
        //String nativeValue = "";
        int when_element_cnt = (Integer) kwargs.getOrDefault("when_element_cnt", 0);
        String convert_string = "";
        List<Element> list = XmlParser.getChildList(child);
        for (Element next_child:list) {
            if("when".equalsIgnoreCase(next_child.getName())){
                if(StringUtils.isNotBlank(nativeValue)&&when_element_cnt>=1){
                    break;
                }else {
                    String test = next_child.attributeValue("test");
                    convert_string += convert_parameters(next_child,true,true);
                    //convert_string += " -- if(" + test + ")";
                    when_element_cnt += 1;
                    kwargs.put("when_element_cnt", when_element_cnt);
                }
            }else if("otherwise".equalsIgnoreCase(next_child.getName())){
                convert_string += convert_parameters(next_child,true,true);
                //convert_string += " -- otherwise";
            }
            convert_string += convert_children(mybatis_mapper, next_child, kwargs);
        }
        return convert_string;
    }

    public static String convert_trim_where_set(Map<String,Element> mybatis_mapper,Element child,Map<String,Object> kwargs){
        String prefix ;
        String suffix ;
        String prefix_overrides ;
        String suffix_overrides;
        if("trim".equalsIgnoreCase(child.getName())){
            prefix = child.attributeValue("prefix");
            suffix = child.attributeValue("suffix");
            prefix_overrides = child.attributeValue("prefixOverrides");
            suffix_overrides = child.attributeValue("suffixOverrides");
        }else if("set".equalsIgnoreCase(child.getName())){
            prefix = " SET";
            suffix = null;
            prefix_overrides = null;
            suffix_overrides = ",";
        }else if("where".equalsIgnoreCase(child.getName())){
            prefix = " WHERE";
            suffix = null;
            prefix_overrides = "and|or";
            suffix_overrides = null;
        }else {
            return "";
        }
        String convert_string = "";
        // Add trim/where/set text
        convert_string += convert_parameters(child,true,false);
        // Convert children first
        List<Element> list = XmlParser.getChildList(child);
        for (Element next_child:list) {
            convert_string += convert_children(mybatis_mapper, next_child, kwargs);
        }
        //# Remove prefixOverrides
        Pattern pattern ;
        Matcher match ;
        if (StringUtils.isNotBlank(prefix_overrides)){
            String rule = "^[\\s]*?(%s)";
            //regex = r'^[\s]*?({})'.format(prefix_overrides)
            String regex = String.format(rule,prefix_overrides);
            pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);//不区分大小写,或者在表达式前面加(?)
            match = pattern.matcher(convert_string);
            convert_string = match.replaceFirst("");

        }
        //# Remove suffixOverrides
        if (StringUtils.isNotBlank(suffix_overrides)){
            String rule = "(%s)(\\s+--.+)?$";
            //regex = r'({})(\s+--.+)?$'.format(suffix_overrides)
            String regex = String.format(rule,suffix_overrides);
            pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);//不区分大小写,或者在表达式前面加(?)
            match = pattern.matcher(convert_string);
            convert_string = match.replaceFirst("");
        }
         //# Add Prefix if String is not empty
        pattern = Pattern.compile("\\S");
        match = pattern.matcher(convert_string);
        if(match.find()){
            if (StringUtils.isNotBlank(prefix)){
                convert_string = " " + prefix + " " + convert_string;
            }
            if (StringUtils.isNotBlank(suffix)){
                convert_string = convert_string + " " + suffix;
            }
        }
        //# Add trim/where/set tail
        convert_string += convert_parameters(child,false,true);
        return convert_string;
    }

    public static String convert_foreach(Map<String,Element> mybatis_mapper,Element child,Map<String,Object> kwargs){
        String collection = child.attributeValue("collection");
        String item = child.attributeValue("item");
        String index = child.attributeValue("index");
        String open = child.attributeValue("open", "");
        String close = child.attributeValue("close", "");
        String separator = child.attributeValue("separator", "");
        String convert_string = " ";
        //# Add foreach text
        convert_string += convert_parameters(child,true,false);
        List<Element> list = XmlParser.getChildList(child);
        for (Element next_child:list) {
            convert_string += convert_children(mybatis_mapper, next_child, kwargs);
        }
        //# Add two items
        convert_string = open + convert_string + separator + convert_string + close;
        //# Add foreach tail
        convert_string += convert_parameters(child,false, true);
        return convert_string;
    }

    public static String convert_bind(Element child,Map<String,Object> kwargs){
        String name = child.attributeValue("name");
        String value = child.attributeValue("value");
        String convert_string = " ";
        convert_string += convert_parameters(child,false,true);
        convert_string = convert_string.replace(name, value);
        return convert_string;
    }
}

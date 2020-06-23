package com.aicat.common.mapper2sql;

import com.aicat.common.utils.XmlParser;
import org.dom4j.Element;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Params {

    private static final String[] NUM = {"TINYINT", "SMALLINT", "INTEGER", "BIGINT", "BIT", "DECIMAL", "DOUBLE", "FLOAT", "NUMERIC"};
    private static final String[] BOOLEAN = {"BOOLEAN"};
    private static final String[] DATE = {"DATE", "TIME", "TIMESTAMP"};
    private static final String[] STRING = {"CHAR", "VARCHAR", "NCHAR", "NVARCHAR", "LONGNVARCHAR", "LONGVARCHAR"};
    private static final String[] BINARY = {"BINARY", "VARBINARY", "LONGVARBINARY", "BLOB"};
    private static final String[] OTHER = {"ARRAY", "CLOB", "CURSOR", "DATALINK", "DATETIMEOFFSET", "DISTINCT", "JAVA_OBJECT", "NCLOB",
            "NULL", "OTHER", "REAL", "REF", "ROWID", "SQLXML", "STRUCT", "UNDEFINED"};

    private static Map<String, Set<String>> jdbc_type = new HashMap<>(6);

    static {
        jdbc_type.put("NUM",new HashSet<>(Arrays.asList(NUM)));
        jdbc_type.put("BOOLEAN",new HashSet<>(Arrays.asList(BOOLEAN)));
        jdbc_type.put("DATE",new HashSet<>(Arrays.asList(DATE)));
        jdbc_type.put("STRING",new HashSet<>(Arrays.asList(STRING)));
        jdbc_type.put("BINARY",new HashSet<>(Arrays.asList(BINARY)));
        jdbc_type.put("OTHER",new HashSet<>(Arrays.asList(OTHER)));
    }

    public static void replace_params(Map<String,String> param){
        String param_jdbc_type = param.get("jdbc_type");
        if (jdbc_type.get("NUM").contains(param_jdbc_type)){
            param.put("mock_value","? ");
        }else if(jdbc_type.get("BOOLEAN").contains(param_jdbc_type)){
            param.put("mock_value","? ");
        }else if(jdbc_type.get("BINARY").contains(param_jdbc_type)){
            param.put("mock_value","? ");
        }else if(jdbc_type.get("STRING").contains(param_jdbc_type)){
            param.put("mock_value","? ");
        }else {
            param.put("mock_value","? ");
        }
    }

    /**
     * example: #{age,javaType=int,jdbcType=NUMERIC,typeHandler=MyTypeHandler}
     * change: '#','$'
     * @param child
     */
    public static Map<String,List<Map<String,String>>> get_params(Element child){
        Pattern pattern = Pattern.compile("\\S");
        // Remove empty info
        //String child_text = StringUtils.isNotBlank(child.getTextTrim()) ? child.getTextTrim() : "";

        //Matcher match = pattern.matcher(child_text) ;
        //String convert_string = match.find()? child_text:"";

        String child_text = XmlParser.getText(child);
        String child_tail = XmlParser.getTail(child);
        Matcher match = pattern.matcher(child_text);
        child_text = match.find() ? child_text : "";
        match = pattern.matcher(child_tail);
        child_tail = match.find() ? child_tail : "";
        String convert_string = child_text + child_tail;

        Map<String,List<Map<String,String>>> params = new HashMap<>(2);
        List<Map<String,String>> symbol3up = new ArrayList<>();
        List<Map<String,String>> symbol_$ = new ArrayList<>();
        params.put("#",symbol3up);
        params.put("$",symbol_$);
        Set<String> keys = params.keySet();
        for(String change:keys){
            List<String> tmp_params = new ArrayList();
            String string_regex = "\\" + change + "\\{.+?\\}";
            //String string_regex2 =  change + "{.+?}";
            pattern = Pattern.compile(string_regex);
            match = pattern.matcher(convert_string);
            while(match.find()){
                tmp_params.add(match.group(0));
            }

            // get jdbcType、javaType
            for (String param:tmp_params ) {
                Map<String,String> param_dict = new HashMap();
                param_dict.put("full_name",param);
                param = param.replace(change + "{", "").replace("}", "");
                param_dict.put("name", param.split(",")[0]);
                pattern = Pattern.compile("(\\s*jdbcType\\s*=\\s*)(\\?P<jdbc_type>\\w+)?");
                match = pattern.matcher(param);
                param_dict.put("jdbc_type",match.find()?match.group("jdbc_type").trim():null );
                pattern = Pattern.compile("(\\s*javaType\\s*=\\s*)\\(?P<java_type>\\w+\\)?");
                match = pattern.matcher(param);
                param_dict.put("java_type", match.find()?match.group("java_type").trim():null );
                //  Replace SQL Params
                replace_params(param_dict);
                List<Map<String,String>> symbol =params.get(change);
                symbol.add(param_dict);
                params.put(change,symbol);
            }
        }
        return params;
    }

    public static void main(String[] args) {
        String rule = "(%s)(\\s+--.+)?$";
        //regex = r'({})(\s+--.+)?$'.format(suffix_overrides)
        String suffix_overrides=",";
        String regex = String.format(rule,suffix_overrides);
        String regex2 = rule.format(suffix_overrides);
        Pattern pattern = Pattern.compile(regex);
        String convert_string = "category = ?, price = ?,";
        Matcher match = pattern.matcher(convert_string);
        convert_string = match.replaceFirst("");
        System.out.println(convert_string);
        Pattern pattern2 = Pattern.compile(regex2);
        String convert_string2 = "category = ?, price = ?,";
        Matcher match2 = pattern2.matcher(convert_string2);
        convert_string2 = match2.replaceFirst("");
        System.out.println(convert_string2);
    }
}

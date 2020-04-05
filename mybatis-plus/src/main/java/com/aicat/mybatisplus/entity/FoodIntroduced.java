package com.aicat.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "food_introduced")//指定表名
public class FoodIntroduced implements Serializable {
    //value与数据库主键列名一致，若实体类属性名与表主键列名一致可省略value
    @TableId(value = "id",type = IdType.AUTO)//指定自增策略
    private Long id;
    //若没有开启驼峰命名，或者表中列名不符合驼峰规则，可通过该注解指定数据库表中的列名，exist标明数据表中有没有对应列
    @TableField(value = "food_id",exist = true)
    private Long food_id;

    private String name;

    private String brief;

    private String label;

    private Date create_date;
    private Date update_date;

    private Byte status;

    private Integer praise;

    private String url;

    private String imgs;

    private Long author;

    private String details;

    private static final long serialVersionUID = 1L;
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", food_id=").append(food_id);
        sb.append(", name=").append(name);
        sb.append(", brief=").append(brief);
        sb.append(", label=").append(label);
        sb.append(", create_date=").append(create_date);
        sb.append(", status=").append(status);
        sb.append(", praise=").append(praise);
        sb.append(", url=").append(url);
        sb.append(", imgs=").append(imgs);
        sb.append(", author=").append(author);
        sb.append(", details=").append(details);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
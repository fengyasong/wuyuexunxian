package com.aicat.mybatisplus.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SceneryIntroduced implements Serializable {
    private Long id;

    private Long scenery_id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScenery_id() {
        return scenery_id;
    }

    public void setScenery_id(Long scenery_id) {
        this.scenery_id = scenery_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief == null ? null : brief.trim();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getPraise() {
        return praise;
    }

    public void setPraise(Integer praise) {
        this.praise = praise;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs == null ? null : imgs.trim();
    }

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details == null ? null : details.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", scenery_id=").append(scenery_id);
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
package com.tianle.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author ：tianLe
 */
@Data
public class Result {
    public String kind;
    public String domain;
    public Source source;
    public String title;
    public Date publishedAt;
    public String slug;
    public Long id;
    public String url;
    public Date createdAt;
    public Votes votes;
    public MetaData metadata;
}


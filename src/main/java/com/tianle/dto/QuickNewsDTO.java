package com.tianle.dto;

import lombok.Data;

import java.util.List;

/**
 * @author ：tianLe
 */
@Data
public class QuickNewsDTO {
    public Boolean success;
    public int count;
    public String next;
    public String previous;
    public List<Result> results;
}


@Data
class Votes {
    public int negative;
    public int positive;
    public int important;
    public int liked;
    public int disliked;
    public int lol;
    public int toxic;
    public int saved;
    public int comments;
}



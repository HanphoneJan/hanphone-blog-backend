package com.example.blog2.vo;

import lombok.Data;

@Data
public class BlogQuery {
    private String title;
    private Long typeId;

    public BlogQuery() {
    }

    @Override
    public String toString() {
        return "BlogQuery{" +
                "title='" + title + '\'' +
                ", typeId=" + typeId +
                '}';
    }
}

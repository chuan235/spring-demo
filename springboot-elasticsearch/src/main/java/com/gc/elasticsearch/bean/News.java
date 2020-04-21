package com.gc.elasticsearch.bean;

import io.searchbox.annotations.JestId;

public class News {

    @JestId
    private Integer id;
    private String content;

    public News() {
    }

    public News(Integer id, String content) {
        this.id = id;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}

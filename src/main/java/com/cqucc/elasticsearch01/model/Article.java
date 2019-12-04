package com.cqucc.elasticsearch01.model;

import java.io.Serializable;

public class Article implements Serializable {

    private static final long serialVersionUID = -1015187669012568585L;
    private Long id;
    private String content;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String conten) {
        this.content = conten;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", title=" + title +
                '}';
    }
}

package com.cqucc.elasticsearch01.dao;

import com.cqucc.elasticsearch01.model.Article;

public interface IArticleDao {

    void addArticle(Article article) throws Exception;

    void findAll() throws Exception;

    void deleteArticle(String id) throws Exception;

    String queryArticleTitle(String title) throws Exception;
}

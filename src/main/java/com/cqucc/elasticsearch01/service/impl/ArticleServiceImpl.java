package com.cqucc.elasticsearch01.service.impl;

import com.cqucc.elasticsearch01.dao.IArticleDao;
import com.cqucc.elasticsearch01.model.Article;
import com.cqucc.elasticsearch01.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements IArticleService {
    @Autowired
    IArticleDao articleDao;

    @Override
    public void addArticle(Article article) throws Exception {
        articleDao.addArticle(article);
    }

    @Override
    public void findAll() throws Exception {
        articleDao.findAll();
    }

    @Override
    public void deleteArticle(String id) throws Exception {
        articleDao.deleteArticle(id);
    }

    @Override
    public String queryArticleTitle(String title) throws Exception {
        return articleDao.queryArticleTitle(title);
    }
}

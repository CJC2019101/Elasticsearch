package com.cqucc.elasticsearch01.controller;


import com.cqucc.elasticsearch01.model.Article;
import com.cqucc.elasticsearch01.service.IArticleService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ArticleController {

    @Autowired
    IArticleService articleService;

    //查询所有
    @RequestMapping("/findAll.do")
    public void findAll() throws Exception {
        articleService.findAll();
    }

    //添加
    @RequestMapping("/addArticle")//添加过后直接查询所有 文档信息
    @ResponseBody
    public String addArticle(@RequestBody Article article) throws Exception {
        articleService.addArticle(article);
        return "添加成功";
//        return "redirect:/findAll.do";
    }

    //删除
    @RequestMapping("/deleteArticle")
    @ResponseBody
    public String deleteArticle(@RequestParam(value = "id") String articleId) throws Exception {
        System.out.println(articleId);
        articleService.deleteArticle(articleId);
        return "删除成功";
    }

    //检索文章标题
    @RequestMapping("/queryArticleTitle")
    @ResponseBody
    public String queryArticleTitle(String title) throws Exception {
        return articleService.queryArticleTitle(title);
    }

}

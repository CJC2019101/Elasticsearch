package com.cqucc.elasticsearch01.dao;

import com.cqucc.elasticsearch01.factory.ClientFactory;
import com.cqucc.elasticsearch01.model.Article;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;


@Repository
public class ArticleDaoImpl implements IArticleDao {

    @Autowired
    ObjectMapper mapper;

    @Override
    public void addArticle(Article article) throws Exception {
        RestHighLevelClient client = ClientFactory.getClient();
        IndexRequest request = new IndexRequest("blog"); //索引
        request.id(String.valueOf(article.getId())); //文档id
        String jsonString =mapper.writeValueAsString(article);
        System.out.println(jsonString);
        request.source(jsonString, XContentType.JSON); //以字符串形式提供的文档源
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
    }

    @Override
    public void findAll() throws Exception {

        RestHighLevelClient client = ClientFactory.getClient();

        MultiSearchRequest request = new MultiSearchRequest(); //创建一个空的MultiSearchRequest 。
        SearchRequest firstSearchRequest = new SearchRequest();//创建一个空的SearchRequest，并像常规搜索一样填充它。
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(matchQuery("user", "kimchy"));
        firstSearchRequest.source(searchSourceBuilder);
        request.add(firstSearchRequest); //将SearchRequest添加到MultiSearchRequest中。
        SearchRequest secondSearchRequest = new SearchRequest();  //构建第二个SearchRequest，并将其添加到MultiSearchRequest中。
        searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(matchQuery("user", "luca"));
        secondSearchRequest.source(searchSourceBuilder);
        request.add(secondSearchRequest);

        //同步执行
        MultiSearchResponse response = client.msearch(request, RequestOptions.DEFAULT);
    }

    @Override
    public void deleteArticle(String id) throws Exception {

        RestHighLevelClient client = ClientFactory.getClient();
        if (client!=null){
            DeleteRequest request = new DeleteRequest(
                    "blog",    //索引
                    id);
            DeleteResponse deleteResponse = client.delete(
                    request, RequestOptions.DEFAULT);
        }//还可以检查文档是否被找到，如果没有执行某些操作


    }

    @Override
    public String queryArticleTitle(String title) throws Exception {
        RestHighLevelClient client = ClientFactory.getClient();
        Map<String,Object> map=null;
        if (client!=null){
            map=new HashMap<>();
            final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
            SearchRequest searchRequest = new SearchRequest("blog");
            searchRequest.scroll(scroll);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(matchQuery("content",title));
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT); //通过发送初始搜索请求来初始化搜索上下文
            String scrollId = searchResponse.getScrollId();
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            for (SearchHit searchHit: searchHits
                 ) {
                map.put(String.valueOf(searchHit.getSourceAsMap().get("title")),searchHit.getSourceAsMap().get("content"));
            }

            while (searchHits != null && searchHits.length > 0) { //通过循环调用搜索滚动api来检索所有搜索命中，直到没有文档返回
                    for (SearchHit searchHit: searchHits
                    ) {
                        map.put(String.valueOf(searchHit.getSourceAsMap().get("title")),searchHit.getSourceAsMap().get("content"));
                    }
                //处理返回的搜索结果
                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId); //创建一个新的搜索滚动请求，保存最后返回的滚动标识符和滚动间隔
                scrollRequest.scroll(scroll);
                searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
                scrollId = searchResponse.getScrollId();
                searchHits = searchResponse.getHits().getHits();

            }
            ClearScrollRequest clearScrollRequest = new ClearScrollRequest(); //完成滚动后，清除滚动上下文
            clearScrollRequest.addScrollId(scrollId);
            ClearScrollResponse clearScrollResponse = client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
            boolean succeeded = clearScrollResponse.isSucceeded();
        }
        if (map!=null){
            Gson gson=new Gson();
            return gson.toJson(map);
        }
        return "失败。";
    }
}

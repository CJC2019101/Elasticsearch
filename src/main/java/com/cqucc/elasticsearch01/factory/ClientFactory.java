package com.cqucc.elasticsearch01.factory;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class ClientFactory {
    private static RestHighLevelClient client = null;
    static {
        client=new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
    }
    public static RestHighLevelClient getClient(){
        return client;
    }

}

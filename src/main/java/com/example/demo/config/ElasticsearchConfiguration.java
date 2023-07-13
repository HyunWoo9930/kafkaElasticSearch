package com.example.demo.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@Configuration
public class ElasticsearchConfiguration extends AbstractElasticsearchConfiguration {

  @NotNull
  @Bean
  public RestHighLevelClient elasticsearchClient() {
    return new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
  }

  @Bean
  public ElasticsearchOperations customElasticsearchTemplate() {
    return new ElasticsearchRestTemplate(elasticsearchClient());
  }
}




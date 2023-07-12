package com.example.demo.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@Configuration
public class ElasticsearchConfiguration extends AbstractElasticsearchConfiguration {

  @Override
  @Bean
  public RestHighLevelClient elasticsearchClient() {
    ClientConfiguration clientConfiguration = ClientConfiguration.builder()
        .connectedTo("localhost:9200")
        .build();

    return RestClients.create(clientConfiguration).rest();
  }

  @Bean
  public ElasticsearchOperations customElasticsearchTemplate() {
    return new ElasticsearchRestTemplate(elasticsearchClient());
  }
}




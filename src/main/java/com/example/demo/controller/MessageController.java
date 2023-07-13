package com.example.demo.controller;

import com.example.demo.dataModel.MessageData;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
@Slf4j
public class MessageController {

  private final RestHighLevelClient restHighLevelClient;

  @Autowired
  public MessageController(RestHighLevelClient restHighLevelClient) {
    this.restHighLevelClient = restHighLevelClient;
  }

  @GetMapping
  public List<MessageData> getAllMessages() throws IOException {
    SearchRequest searchRequest = new SearchRequest("test-index");
    searchRequest.source().query(QueryBuilders.matchAllQuery());
    // elasticsearch가 표시할 hit 수
    searchRequest.source().size(100);
    // elasticsearch sentTime으로 내림차순 - 최신 hit 표시
    searchRequest.source().sort("sentTime", SortOrder.DESC);

    SearchResponse searchResponse = restHighLevelClient.search(searchRequest,
        RequestOptions.DEFAULT);

    List<MessageData> messages = new ArrayList<>();
    for (SearchHit hit : searchResponse.getHits().getHits()) {
      String sourceAsString = hit.getSourceAsString();
      // Elasticsearch에서 가져온 데이터를 MessageData 객체로 변환하여 리스트에 추가하는 로직
      MessageData messageData = convertToMessageData(sourceAsString);
      messageData.setId(hit.getId());
      messages.add(messageData);
    }

    return messages;
  }

  // Elasticsearch에서 가져온 데이터를 MessageData 객체로 변환하는 메서드
  private MessageData convertToMessageData(String source) {
    // 변환 로직 작성
    JsonObject object = new JsonParser().parse(source).getAsJsonObject();
    MessageData messageData = new MessageData();
    messageData.setMessage(object.get("message").getAsString());
    messageData.setSentTime(convertToLocalDateTime(object.get("sentTime").getAsString()));
    return messageData;
  }

  private LocalDateTime convertToLocalDateTime(String sentTime) {
    // sentTime 값을 LocalDateTime으로 변환하는 로직 작성
    // 예시: Epoch 밀리초 값을 사용하여 LocalDateTime으로 변환
    long epochMillis = Long.parseLong(sentTime);
    Instant instant = Instant.ofEpochMilli(epochMillis);
    return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
  }
}


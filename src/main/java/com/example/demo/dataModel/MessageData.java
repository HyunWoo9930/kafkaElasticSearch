package com.example.demo.dataModel;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "test-index")
@Getter
@Setter
public class MessageData {

  @Id
  private String id;
  private String message;

  // Elasticsearch에 저장할 데이터 모델 객체 생성
  public static MessageData create(String message) {
    MessageData dataModel = new MessageData();
    dataModel.setMessage(message);
    return dataModel;
  }
}


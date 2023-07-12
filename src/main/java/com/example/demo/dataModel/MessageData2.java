package com.example.demo.dataModel;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "test-index2")
@Getter
@Setter
public class MessageData2 {

  @Id
  private String id;
  private String message;

  // Elasticsearch에 저장할 데이터 모델 객체 생성
  public static MessageData2 create(String message) {
    MessageData2 dataModel = new MessageData2();
    dataModel.setMessage(message);
    return dataModel;
  }
}


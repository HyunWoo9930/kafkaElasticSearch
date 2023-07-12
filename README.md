# kafkaElasticSearch

Kafka &amp; Elastic Search 세팅 및 테스트

### Spring kafka , Kafka Stream
 * Spring Kafka는 Spring Framework와 Kafka의 통합을 간편하게 제공하고, 주로 Kafka 프로듀서와 컨슈머의 구성에 중점을 둡니다.<br> 
반면에 Kafka Streams는 Kafka에서의 데이터 스트림 처리에 초점을 맞추고, 데이터 변환, 집계, 필터링 등의 작업을 수행할 수 있는 라이브러리입니다.

### Zookeeper
  1. 클러스터 코디네이션: <br>
      ZooKeeper는 분산 시스템에서 클러스터의 구성 정보를 저장하고 관리합니다.
      Kafka 클러스터의 브로커, 토픽, 파티션, 컨슈머 그룹 등의 구성 정보를 ZooKeeper에 저장하여 Kafka 브로커 간에 일관된 상태를 유지합니다.
  2. 리더 선출: <br>
      Kafka의 토픽은 여러 파티션으로 분할될 수 있습니다. 각 파티션은 리더와 팔로워로 구성되는데, 리더는 데이터를 수신하고 처리하는 역할을 담당합니다.
      ZooKeeper는 리더 선출을 조정하여 Kafka 클러스터에서 각 파티션의 리더를 결정합니다.
      리더 선출을 통해 고가용성과 장애 복구를 보장할 수 있습니다.
  3. 상태 모니터링: <br>
    ZooKeeper는 Kafka 클러스터의 상태를 모니터링하고 감지할 수 있습니다.
    브로커, 토픽, 파티션 등의 상태 변경이 발생하면 ZooKeeper는 이를 감지하고 Kafka 클러스터에 해당 변경 사항을 알립니다. 
  4. 데이터 일관성: <br>
    ZooKeeper는 ACID 원칙에 따라 데이터 일관성을 보장합니다.
    Kafka의 메타데이터와 구성 정보는 ZooKeeper에 저장되므로, ZooKeeper는 데이터의 정확성과 일관성을 유지하면서 Kafka 클러스터를 조정합니다.

### Kafka-ElasticSearch 에서 데이터 흐름
1. Kafka 프로듀서에서 데이터가 생성 되며 Kafka 토픽으로 전송.
2. Kafka 브로커는 토픽 내 파티션에 데이터를 저장. 이때, Kafka 클러스터는 내부적으로 ZooKeeper를 사용. <br> ZooKeeper는 분산 시스템에서 클러스터의 구성 정보를 저장하고 관리하며, Kafka 클러스터의 브로커, 토픽, 파티션, 컨슈머 그룹 등의 구성 정보를 저장하여 Kafka 브로커 간에 일관된 상태를 유지.               
3. Elasticsearch 커넥터는 Kafka 토픽에서 데이터를 읽고 Elasticsearch로 전송.
4. Elasticsearch는 데이터를 인덱스에 저장.
5. 데이터는 Elasticsearch를 사용하여 쿼리하고 분석할 수 있다.

### Kafka 동작 방식

Kafka는 분산 메시지 큐 시스템으로, 대용량의 실시간 로그 처리에 적합합니다. Kafka는 데이터를 브로커에 저장하고, 컨슈머는 이를 읽어들여 데이터를 처리합니다.

Kafka에서 데이터는 토픽 단위로 관리됩니다. 하나의 토픽은 여러 개의 파티션으로 분할될 수 있으며, 각 파티션은 브로커에 저장됩니다. 이때, 데이터는 순서가 보장됩니다.

Kafka의 동작은 다음과 같습니다.

1. 프로듀서에서 데이터 생성 및 전송: 데이터를 생성하여 Kafka 토픽으로 전송합니다.
2. 브로커에서 데이터 저장: Kafka 브로커는 토픽 내 파티션에 데이터를 저장합니다.
3. 컨슈머에서 데이터 처리: 컨슈머는 브로커에서 데이터를 읽어들여 처리합니다.

Kafka는 고성능 및 확장성을 보장하기 위해 멀티스레드 아키텍처를 사용합니다. 따라서, Kafka 클러스터에서 여러 개의 브로커가 동시에 데이터를 처리할 수 있습니다.

- Kafka Producer

    ```java
    @Component
    public class KafkaProducer {
    
      private final KafkaTemplate<String, String> kafkaTemplate;
    
      @Autowired
      public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
      }
    
      public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
      }
    }
    ```

- Kafka Consumer

    ```java
    @Component
    @Slf4j
    public class KafkaConsumer {
    
      @KafkaListener(topics = "your-topic-name")
      public void receiveMessage(String message) {
        log.info("Received message: {}", message);
      }
    }
    ```

### Kafka-Elasticsearch 연동

1. ElasticsearchConfig 생성 → 미리 생성해 두었던 Localhost:9200과 연결해주는 작업
    - code example

        ```java
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
        ```

        - ‘elasticsearchClient()’ 메서드를 오버라이드하여 Elasticsearch 클라이언트를 구성 RestHighLevelClient 객체를 생성하고 Elasticsearch 클러스터(localhost:9200)에 연결 해줌
2. KafkaConsumer에서 kafka에 message가 도착한것을 KafkaListener로 체크
    - code example

        ```java
        @Component
        @Slf4j
        public class KafkaConsumer {
        
          private final ElasticsearchOperations elasticsearchOperations;
        
          @Autowired
          public KafkaConsumer(ElasticsearchOperations elasticsearchOperations) {
            this.elasticsearchOperations = elasticsearchOperations;
          }
        
          @KafkaListener(topics = "test-topic")
          public void receiveMessage(String message) {
            MessageData data = MessageData.create(message);
            elasticsearchOperations.save(data);
            log.info("Message indexed: {}", message);
          }
        
          @KafkaListener(topics = "test-topic2")
          public void receiveMessage2(String message) {
            MessageData2 data = MessageData2.create(message);
            elasticsearchOperations.save(data);
            log.info("Message indexed: {}", message);
          }
        }
        ```

3. message를 Elasticsearch data 형식으로 변환.
    - Data example

        ```java
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
        ```

4. data를 ElasticsearchOperations에 저장.
    1. ElasticsearchOperations란?
        - ElasticsearchOperations는 Spring Data Elasticsearch에서 제공하는 인터페이스
        - ElasticsearchOperations를 사용하여 Elasticsearch와 데이터를 색인하고 검색하며, 다양한 CRUD(Create, Read, Update, Delete) 작업을 수행
5. Elasticsearch에 제대로 적제 되었는지 확인
    1. [http://localhost:9200/{index}/_search](http://localhost:9200/index/_search) 해서, value값이 제대로 올라가는지 확인.
        - sample
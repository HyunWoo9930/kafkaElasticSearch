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
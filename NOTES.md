## APACHE KAFKA SERIES: Kafka For Beginners v2 Edition

- Culled from a LinkedIn/DataCumulus class. Stephane Maarek. Author of the Apache Kafka Series
- https://github.com/simplesteph

- INTRODUCTION:

  - Source System -> Data -> Target System.
  - If you have four (4) source system and six (6) target systems you need to write twenty-four (24) integrations.
  - Each integration:
    1. Protocol: TCP, HTTP, REST, FTP, JDBC.
    2. Data FOrmat: Binary, CSV, JSON, Avro.
    3. Data Schema & Evolution: Data shape(s) and associated modifications.
  - Each source system will have an increased load from the connections.

  - Apache Kafka: Decoupling of data streams and systems. "A high-throughput distributed messaging system."

  - Why? Created by LinkedIn. Mainly maintained by COnfluent.
  - Distributed, resilient architecture, fault tolerant.
  - Horizontal scalability: 100s of brokers. Millions of messages per second.
  - High performance with a latency less than 10 milliseconds. Real time.

  - Use cases:
    - Messaging system. Activity tracking. Metric gathering. Application log gathering.
    - Stream processing. (e.g.: Kafka Streams API or Spark.) Decoupling of system dependencies.
    - Big data integrations. (e.g.: Spark, Flink, Storm, Hedoop.)
  - e.g.:
    - _Netflix_ uses Kafka to apply recommendations in real-time while you're watching.
    - _Uber_ uses Kafka to gather user, taxi, and trip data to compute and forecast demand and to compute surge pricing in realtime.

- THEORY:

  - Topic: A particular stream of data. Similiar to a table in a database without the constraints.
    - X number of topics. A topic is identified by its name.
    - Topics are split into partitions. Starting at 0.
    - Each partition is ordered. Each message within a partition gets an incremental id called an offset.
    - Know your partitions when you create a topic. First message/offset 0. Second message/offset 1.
    - Offset means nothing on its own. Topic X. Partition 0. Offset 40.
  - e.g.:

    - truck_gps: "A fleet of trucks whereby each truck reports its GPS position to Kafka."
    - Each truck will send a message to Kafka every 20 seconds, each message will contain the truck id and the truck position (latitude and longitude.)
    - We choose to create that topic with 10 partitions. (Arbitrary.)
    - Subscribers: Location Dashboard. Notification Service.
    - _Gotchas:_
      - Offset only has a meaning for a specific partition.
      - Order is guarenteed only within a partition. Not across partitions. Otherwise, you must read messages in order to know order.
      - Data is kept only for a limited time. Default is only one week.
      - Once the data is written to a partition, it cannot be changed. Immutability.
      - Data is assigned randomly to a partition unless a key is provided.

  - Broker:

    - A _Kafka cluster_ is composed of multiple brokers (servers.) Cluster = multiple machines. Broker = server.
    - Each broker is identified with its id (integer.)
    - Each broken contains certain topic partitions.
    - After connecting to an broker (called a bootstrap broker,) you will be connected to the entire cluster.
    - A good number of brokers to start with is three (3) but some big clusters have over 100.

  - Topic Replication Factor:

    - Kafka is a distributed system. Replication is needed.
    - Topics should have a replication factor > 1. Usually beteween 2 and 3. Three is the "gold standard."
    - Replication ensures that data can still be served.
    - Concept of a leader for a partition. At any time only one broker can be a leader fir a given partition.
    - Only that leader can receive and serve data for a partition.
    - The onther brokers will synchronize the data. Therefore, each partition has one leader and multiple ISR (In-sync Replica.)

  - Producers:

    - Producers write data to topics (which is made of partitions.)
    - Producers automatically know to which broker and partition to write to. In case of broker failures, producers automatically recover.
    - The producer will load balance. Producer can choose confirmation.

      1. acks=0: Producer will not wait for acknowledgement. Possible data loss.
      2. acks=1: Default. Producer will wait for the leader to acknowledge. Limited data loss.
      3. acks=all: Leader and all replicas acknowledge. No data loss.

    - Producers can choose to send a key with a message. Message keys can be aything you want: String, number...
    - If key is null then data is sent round-robin: Broker 101, then 102, then 103...
    - If key is sent, all of the messages for that key will always go to the same partition.
    - A key is basically sent if you need message ordering for a specific field. e.g.: truck_id.
    - Advanced: We receive this guarentee thanks to key hashing, which depends on the number of partitions.

  - Consumers:
    -

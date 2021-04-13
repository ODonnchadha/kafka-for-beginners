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

    - Consumers read data from a topic (identified by name.)
    - Consumers know which broker to read from.
    - In case of broker failures, consumers know how to recover.
    - Data is read in order _within each partition._
    - No guarentee in order across partitions.

    - Consumer groups: Consumers read data in consumer groups.
    - Each consumer within a group will read from exclusive partitions.
    - NOTE: If you have more consumers than partitions, some consumers will be inactive.
    - Typically, have as many consumers, at most, as partitions.

  - Consumer Offsets:

    - Kafka stores the offsets at which a consumer group has been reading. e.g.: Bookmarking.
    - The offsets committed live in a Kafka topic named \_\_consumer_offsets.
    - When a consumer in a group has processed data received from Kafka, it should be committing the offsets.
    - If a consumer dies, it will be able to read back exactly from where it left off thanks to the committed consumer offsets.
    - Delivery semantics for consumers:
      - Consumers choose when to commit offsets. Three delivery semantics:
        1. At most once:
        - Offsets are committed as soon as the message is received.
        - If the processing goes wrong, the message will be lost. It won't be read again.
        2. At least once (usually preferred):
        - Offsets are committed after the message is processed.
        - If the processing goes wrong, the message will be read again.
        - This can result in duplicate processing of messages. Ensure that your processing is idempotent.
        3. Exactly once:
        - Can be achived for Kafka-to-Kafka workflows using Kafka Streams API.
        - For Kafka-to-External systrem workflows, use an idempotent consumer.

  - Kafka Broker Discovery:

    - Every Kafka broker is also called a "bootstrap server."
    - This means that you only need to connect to one broker and you will be connected to the entire cluster.
    - Each broker knows about all brokers, topics, and partitions (metadata.)
    - Kafka Client:
      1. Connection & metadata Request.
      2. Receives list of all brokers.
      3. Can connect to the needed brokers.

  - Zookeeper:

    - Zookeeper manages brokers keeping a list of them.
    - Zookeeper helps in performing leader election for partitions.
    - Zookeeper sends notificationds to Kafka in the event of changes. e.g.: New topic, dead broker.
    - Kafka cannot function without Zookeeper.
    - Zookeeper, by design, operates with an odd number of sewrvers.
    - Zookeeper has a leader which handles writes and the rest of the servers are followers which handle reads.
    - Zookeeper does _not_ store consumer offsetds with Kafka. (> v0.10.)

  - Kafka Guarentees:
    - Messages are appended to a topic/partition in the order they are sent.
    - Consumers read messages in the order stored in a topic/partition.
    - With a replication factor of N, producers and consumers can tolerate up to N-1 brokers being down.
    - This is why a replication factotr of 3 is a good idea:
      - Allows for one broker to be taken down for maintenance.
      - Allows for another broker to be taken down unexpectedly.
    - As long as the number of partitions remain coinstant for a topic the same key will always go to the same partition.

- STARTING KAFKA:
  - Windows download and setup:
    1. Download and install Java SE Development Kit 8.
    2. Download the Kafka binaries.
    3. Within a command prompt:
    ```javascript
      C:\Kafka\bin>cd windows
      C:\Kafka\bin\windows>kafka-topics.bat
    ```
    4. Add 'C:\Kafka\bin\windows' to Environment Variables PATH.
    5. Create some subfolders:
    - C:\Kafka\data\kafka
    - C:\Kafka\data\zookeeper
    6. Edit configuration:
    - config\zookeeper.properties
    7. Run zookeeper
    ```javascript
      C:\>zookeeper-server-start.bat C:\Kafka\config\zookeeper.properties
    ```
    8. Edit properties:
    - config\server.properties
    ```javascript
      C:\>kafka-server-start.bat C:\Kafka\config\server.properties
    ```

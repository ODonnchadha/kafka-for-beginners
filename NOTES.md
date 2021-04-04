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
    - *Netflix* uses Kafka to apply recommendations in real-time while you're watching.
    - *Uber* uses Kafka to gather user, taxi, and trip data to compute and forecast demand and to compute surge pricing in realtime.


- THEORY:
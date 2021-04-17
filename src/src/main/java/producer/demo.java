package producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class demo {
    public static void main(String[] args) {
        // 1.0 Create producer properties.
        Properties properties = new Properties();

        // "bootstrap.servers"
        properties.setProperty(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        // How are we going to serialize the bytes? Using a string serializer.
        // "key.serializer"
        properties.setProperty(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        // "value.serializer"
        properties.setProperty(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());

        // 2.0 Create producer.
        KafkaProducer<String, String> producer =
                new KafkaProducer<String, String>(properties);

        // 2.1 Create a producer record.
        ProducerRecord<String, String> record =
                new ProducerRecord<String, String>(
                        "java_producer_topic", "Hello World!");

        // 3.0 Send data.
        producer.send(record);
    }
}

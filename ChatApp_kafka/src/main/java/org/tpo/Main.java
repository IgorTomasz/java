package org.tpo;

import kafka.server.KafkaConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import scala.Int;

import javax.swing.*;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;



public class Main {

    public static Map<Integer,String> activeUsers = new HashMap<>();

    public static void main(String[] args) {
        EmbeddedKafkaBroker embeddedKafkaBroker = new EmbeddedKafkaBroker(1,true,1).kafkaPorts(9092);

/*        embeddedKafkaBroker.brokerProperties(Map.of("listeners","PLAINTEXT://192.168.0.27:9092",
                "advertised.listeners","PLAINTEXT://192.168.0.27:9092"));*/
        embeddedKafkaBroker.afterPropertiesSet();

        SwingUtilities.invokeLater(()->new Chat("Chat-1"));
        SwingUtilities.invokeLater(()->new Chat("Chat-2"));




    }
}

class MessageSender{
    private static KafkaProducer<String,String> kafkaProducer = new KafkaProducer<>(
            Map.of(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName(),
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()
            )
    );

    public static void send(ProducerRecord<String,String> message){
        kafkaProducer.send(message);
    }
}
class MessageReceiver{
    KafkaConsumer<String,String> kafkaConsumer;

    public MessageReceiver(String topic, String id){
        kafkaConsumer = new KafkaConsumer<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                        ConsumerConfig.GROUP_ID_CONFIG, id,
                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName(),
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName(),
                        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true
                )
        );
        kafkaConsumer.subscribe(Collections.singletonList(topic));

        kafkaConsumer.poll(Duration.of(1, ChronoUnit.SECONDS)).forEach(cr-> System.out.println(id+": "+cr.value()));
    }
}
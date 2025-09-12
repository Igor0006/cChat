package com.cchat.ingress_service.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;


import org.springframework.kafka.support.serializer.JsonSerializer;

import com.cchat.ingress_service.model.MessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class KafkaProducerConfig {
    @Bean
    public ProducerFactory<String, MessageDto> producerFactory(ObjectMapper mapper) {
        Map<String, Object> configProperties = new HashMap<>();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        JsonSerializer<MessageDto> serializer = new JsonSerializer<>(mapper);
        serializer.setAddTypeInfo(false);

        return new DefaultKafkaProducerFactory<>(configProperties, new StringSerializer(), serializer);
    }

    @Bean
    public KafkaTemplate<String, MessageDto> kafkaTemplate(ProducerFactory<String, MessageDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}

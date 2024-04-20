package com.github.vitaliiev.t1kafka.consumer.config;

import com.github.vitaliiev.t1kafka.consumer.model.actuate.health.SystemHealthDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.DelegatingByTypeSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

	@Bean
	public NewTopic metricTopic() {
		return TopicBuilder.name("metrics-topic")
				.partitions(1)
				.replicas(1)
				.build();
	}

	@Bean
	public CommonErrorHandler errorHandler(KafkaOperations<Object, Object> kafkaOperations) {
		DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaOperations);
		return new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 2));
	}

	@Bean
	public DefaultKafkaProducerFactoryCustomizer producerFactoryCustomizer() {
		return pf -> ((DefaultKafkaProducerFactory<Object, Object>) pf)
				.setValueSerializer(getDelegatingByTypeSerializer());
	}

	private DelegatingByTypeSerializer getDelegatingByTypeSerializer() {
		return new DelegatingByTypeSerializer(Map.of(
				SystemHealthDto.class, new JsonSerializer<>(),
				byte[].class, new ByteArraySerializer(),
				String.class, new StringSerializer()
		));
	}

}

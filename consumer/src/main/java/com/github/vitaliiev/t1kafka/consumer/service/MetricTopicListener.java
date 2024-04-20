package com.github.vitaliiev.t1kafka.consumer.service;

import com.github.vitaliiev.t1kafka.consumer.model.actuate.health.SystemHealthDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Component
public class MetricTopicListener {

	private static final Logger log = LoggerFactory.getLogger(MetricTopicListener.class);

	private final HealthRecordService healthRecordService;

	public MetricTopicListener(@Qualifier("failingHealthRecordService") HealthRecordService healthRecordService) {
		this.healthRecordService = healthRecordService;
	}

	@KafkaListener(id = "t1kafkaConsumer", topics = "metrics-topic")
	@RetryableTopic(backoff = @Backoff(maxDelay = 6000, multiplier = 2))
	public void systemHealthHandler(@Header(value = KafkaHeaders.RECEIVED_KEY) String serviceName,
	                                @Header(value = KafkaHeaders.RECEIVED_TIMESTAMP) Long createdAt,
	                                @Payload SystemHealthDto systemHealth) {
		OffsetDateTime createdAtTimestamp = Instant.ofEpochMilli(createdAt).atOffset(ZoneOffset.UTC);
		healthRecordService.saveSystemHealth(systemHealth, serviceName, createdAtTimestamp);
	}

	@DltHandler
	public void dltHandler(@Headers Map<String, Object> headers) {
		log.info("Received from DLT: topic {}, partition: {}, offset: {}, service: {}, with error message:\n {}",
				getHeaderOrDefauilt(headers, KafkaHeaders.ORIGINAL_TOPIC,"-"),
				getHeaderOrDefauilt(headers, KafkaHeaders.ORIGINAL_PARTITION,"-"),
				getHeaderOrDefauilt(headers, KafkaHeaders.ORIGINAL_OFFSET,"-"),
				getHeaderOrDefauilt(headers, KafkaHeaders.RECEIVED_KEY,"unknown"),
				getHeaderOrDefauilt(headers, KafkaHeaders.EXCEPTION_MESSAGE,"-"));
	}

	private String getHeaderOrDefauilt(Map<String, Object> headers, String header, String defaultValue) {
		Object value = headers.get(header);
		if (value == null) {
			return defaultValue;
		} else if (value instanceof byte[] byteArr) {
			return new String(byteArr);
		} else {
			return value.toString();
		}
	}
}

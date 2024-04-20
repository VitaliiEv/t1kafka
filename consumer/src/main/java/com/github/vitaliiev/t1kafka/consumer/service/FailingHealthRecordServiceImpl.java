package com.github.vitaliiev.t1kafka.consumer.service;


import com.github.vitaliiev.t1kafka.consumer.model.HealthRecord;
import com.github.vitaliiev.t1kafka.consumer.model.actuate.health.SystemHealthDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Успешный в 25% случаев сервися обработки сообщений
 */
@org.springframework.stereotype.Service
@Qualifier("failingHealthRecordService")
public class FailingHealthRecordServiceImpl implements HealthRecordService {

	private final HealthRecordService delegate;

	public FailingHealthRecordServiceImpl(@Qualifier("successfulHealthRecordService") HealthRecordService delegate) {
		this.delegate = delegate;
	}

	@Override
	@Transactional
	public List<HealthRecord> saveSystemHealth(SystemHealthDto health, String serviceName, OffsetDateTime createdAt) {
		int fail = ThreadLocalRandom.current().nextInt(4);
		if (fail != 0) {
			throw new RuntimeException("Oops");
		}
		return delegate.saveSystemHealth(health, serviceName, createdAt);
	}


}

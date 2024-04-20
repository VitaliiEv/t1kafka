package com.github.vitaliiev.t1kafka.consumer.service;

import com.github.vitaliiev.t1kafka.consumer.model.HealthRecord;
import com.github.vitaliiev.t1kafka.consumer.model.actuate.health.SystemHealthDto;

import java.time.OffsetDateTime;
import java.util.List;

public interface HealthRecordService {

	List<HealthRecord> saveSystemHealth(SystemHealthDto health, String serviceName, OffsetDateTime createdAt);
}

package com.github.vitaliiev.t1kafka.consumer.service;

import com.github.vitaliiev.t1kafka.consumer.model.HealthRecordDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MetricsService {


	Optional<HealthRecordDto> metric(UUID id);

	List<HealthRecordDto> metrics(Integer page, String service, String component, String status);
}

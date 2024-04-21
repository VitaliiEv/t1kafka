package com.github.vitaliiev.t1kafka.consumer.service;


import com.github.vitaliiev.t1kafka.consumer.model.HealthRecord;
import com.github.vitaliiev.t1kafka.consumer.model.Service;
import com.github.vitaliiev.t1kafka.consumer.model.actuate.health.CompositeHealthDto;
import com.github.vitaliiev.t1kafka.consumer.model.actuate.health.SystemHealthDto;
import com.github.vitaliiev.t1kafka.consumer.repository.HealthRecordRepository;
import com.github.vitaliiev.t1kafka.consumer.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Qualifier("successfulHealthRecordService")
public class HealthRecordServiceImpl implements HealthRecordService {

	private final ServiceRepository serviceRepository;
	private final HealthRecordRepository healthRecordRepository;

	@Override
	@Transactional
	public List<HealthRecord> saveSystemHealth(SystemHealthDto health, String serviceName, OffsetDateTime createdAt) {
		Service service = findOrCreateService(serviceName);
		Consumer<HealthRecord> customizer = r -> {
			r.setCreatedAt(createdAt);
			r.setService(service);
		};
		List<HealthRecord> result = new ArrayList<>();
		HealthRecord healthRecord = new HealthRecord();
		customizer.accept(healthRecord);
		healthRecord.setComponent("system");
		healthRecord.setStatus(health.getStatus().getCode());
		result.add(healthRecord);
		if (health.getComponents() != null) {
			List<HealthRecord> healthRecords = aggregateRecords(health.getComponents(), customizer);
			result.addAll(healthRecords);
		}
		if (health.getDetails() != null) {
			addDetails(healthRecord, health.getDetails());
		}
		return healthRecordRepository.saveAll(result);
	}

	private Service findOrCreateService(String name) {
		return serviceRepository.findByName(name)
				.orElseGet(() -> createService(name));
	}

	private Service createService(String name) {
		Service service = new Service();
		service.setName(name);
		return serviceRepository.save(service);
	}

	private List<HealthRecord> aggregateRecords(Map<String, CompositeHealthDto> components,
	                                            Consumer<HealthRecord> customizer) {
		List<HealthRecord> result = new ArrayList<>();
		for (Map.Entry<String, CompositeHealthDto> entry : components.entrySet()) {
			List<HealthRecord> healthRecords = fromCompositeHealth(entry.getKey(), entry.getValue(), customizer);
			result.addAll(healthRecords);
		}
		return result;
	}

	private void addDetails(HealthRecord parent, Map<String, Object> components) {
		Map<String, String> details = components.entrySet()
				.stream()
				.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
		if (parent.getDetails() == null) {
			parent.setDetails(new HashMap<>());
		}
		parent.getDetails().putAll(details);
	}

	private List<HealthRecord> fromCompositeHealth(String name, CompositeHealthDto compositeHealth,
	                                               Consumer<HealthRecord> customizer) {
		List<HealthRecord> result = new ArrayList<>();
		HealthRecord healthRecord = new HealthRecord();
		customizer.accept(healthRecord);
		healthRecord.setComponent(name);
		healthRecord.setStatus(compositeHealth.getStatus().getCode());
		result.add(healthRecord);
		if (compositeHealth.getComponents() != null) {
			List<HealthRecord> healthRecords = aggregateRecords(compositeHealth.getComponents(), customizer);
			result.addAll(healthRecords);
		}
		if (compositeHealth.getDetails() != null) {
			addDetails(healthRecord, compositeHealth.getDetails());
		}
		return result;
	}

}

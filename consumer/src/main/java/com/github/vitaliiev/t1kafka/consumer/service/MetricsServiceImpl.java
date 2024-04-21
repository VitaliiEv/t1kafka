package com.github.vitaliiev.t1kafka.consumer.service;

import com.github.vitaliiev.t1kafka.consumer.model.HealthRecord;
import com.github.vitaliiev.t1kafka.consumer.model.HealthRecordDto;
import com.github.vitaliiev.t1kafka.consumer.model.Service;
import com.github.vitaliiev.t1kafka.consumer.model.ServiceDto;
import com.github.vitaliiev.t1kafka.consumer.repository.HealthRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.*;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class MetricsServiceImpl implements MetricsService {

	private final HealthRecordRepository repository;

	@Override
	public Optional<HealthRecordDto> metric(UUID id) {
		return repository.findById(id)
				.map(r -> this.toHealthRecordDto(r, toServiceDto(r.getService())));
	}

	@Override
	public List<HealthRecordDto> metrics(Integer page, String service, String component, String status) {
		Example<HealthRecord> example = getExample(service, component, status);
		PageRequest pageRequest = getPageRequest(page);
		Map<UUID, ServiceDto> services = new HashMap<>();
		return repository.findAll(example, pageRequest)
				.stream()
				.map(r -> {
					Service s = r.getService();
					ServiceDto serviceDto = services.computeIfAbsent(s.getId(), id -> toServiceDto(s));
					return toHealthRecordDto(r, serviceDto);
				})
				.toList();
	}

	private Example<HealthRecord> getExample(String serviceName, String component, String status) {
		HealthRecord healthRecord = new HealthRecord();
		Service service = new Service();
		service.setName(serviceName);
		healthRecord.setService(service);
		healthRecord.setComponent(component);
		healthRecord.setStatus(status);
		return Example.of(healthRecord, ExampleMatcher.matching().withIgnoreNullValues());
	}

	private PageRequest getPageRequest(Integer page) {
		if (page == null || page <0) {
			page = 0;
		}
		Sort sort = Sort.by(Sort.Order.desc("createdAt"));
		return PageRequest.of(page, 100, sort);
	}

	private HealthRecordDto toHealthRecordDto(HealthRecord healthRecord, ServiceDto serviceDto) {
		return new HealthRecordDto()
				.id(healthRecord.getId())
				.service(serviceDto)
				.component(healthRecord.getComponent())
				.status(healthRecord.getStatus())
				.createdAt(healthRecord.getCreatedAt().toLocalDateTime())
				.details(healthRecord.getDetails());

	}

	private ServiceDto toServiceDto(Service service) {
		return new ServiceDto()
				.id(service.getId())
				.name(service.getName());
	}

}

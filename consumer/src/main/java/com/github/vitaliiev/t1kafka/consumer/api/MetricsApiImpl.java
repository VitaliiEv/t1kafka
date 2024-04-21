package com.github.vitaliiev.t1kafka.consumer.api;

import com.github.vitaliiev.t1kafka.consumer.model.HealthRecordDto;
import com.github.vitaliiev.t1kafka.consumer.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MetricsApiImpl implements MetricsApiDelegate {

	private final MetricsService metricsService;

	@Override
	public ResponseEntity<HealthRecordDto> metric(UUID id) {
		return metricsService.metric(id)
				.map(ResponseEntity::ok)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Override
	public ResponseEntity<List<HealthRecordDto>> metrics(Integer page, String service, String component,
	                                                     String status) {
		List<HealthRecordDto> metrics = metricsService.metrics(page, service, component, status);
		return ResponseEntity.ok(metrics);
	}

}

package com.github.vitaliiev.t1kafka.producer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.ApiVersion;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.endpoint.web.WebServerNamespace;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpointWebExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "metrics")
public class TaskController {

	private final HealthEndpointWebExtension healthEndpointWebExtension;
	private final KafkaTemplate<String, Object> template;

	@Value("${spring.application.name}")
	private String applicationName;

	@PostMapping
	public void metrics() {
		WebEndpointResponse<HealthComponent> health = healthEndpointWebExtension.health(ApiVersion.LATEST,
				WebServerNamespace.SERVER, SecurityContext.NONE, true);
		HealthComponent body = health.getBody();
		template.send("metrics-topic", applicationName, body);
	}

}

package com.github.vitaliiev.t1kafka.consumer.model.actuate.health;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class HealthDto extends HealthComponentDto {

	private Map<String, Object> details;
}

package com.github.vitaliiev.t1kafka.consumer.model.actuate.health;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class CompositeHealthDto extends HealthComponentDto {

	private Map<String, CompositeHealthDto> components;

	@JsonProperty
	private Map<String, Object> details;

}

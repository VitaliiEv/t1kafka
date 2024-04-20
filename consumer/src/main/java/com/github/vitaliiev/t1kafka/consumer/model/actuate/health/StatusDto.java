package com.github.vitaliiev.t1kafka.consumer.model.actuate.health;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatusDto {

	@JsonProperty("status")
	private String code;

	private String description;
}

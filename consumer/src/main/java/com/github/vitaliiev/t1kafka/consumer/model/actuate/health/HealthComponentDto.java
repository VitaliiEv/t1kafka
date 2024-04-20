package com.github.vitaliiev.t1kafka.consumer.model.actuate.health;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HealthComponentDto {

	@JsonUnwrapped
	private StatusDto status;

}

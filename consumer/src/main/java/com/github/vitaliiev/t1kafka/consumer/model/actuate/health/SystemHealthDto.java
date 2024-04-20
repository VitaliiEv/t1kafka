package com.github.vitaliiev.t1kafka.consumer.model.actuate.health;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SystemHealthDto extends CompositeHealthDto {

	private Set<String> groups;

}

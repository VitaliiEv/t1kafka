package com.github.vitaliiev.t1kafka.consumer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Service {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotNull
	@Column(name = "SERVICE_NAME", nullable = false, unique = true)
	private String name;

	@OneToMany(mappedBy = "service")
	private List<HealthRecord> records;

}

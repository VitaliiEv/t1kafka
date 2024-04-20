package com.github.vitaliiev.t1kafka.consumer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
public class HealthRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	@NotNull
	private OffsetDateTime createdAt;

	@ManyToOne(optional = false)
	@JoinColumn(name = "SERVICE_ID", nullable = false)
	private Service service;

	@NotNull
	@Column(nullable = false)
	private String component;

	@NotNull
	@Column(nullable = false, length = 32)
	private String status;

	@ElementCollection
	private List<String> details;

}

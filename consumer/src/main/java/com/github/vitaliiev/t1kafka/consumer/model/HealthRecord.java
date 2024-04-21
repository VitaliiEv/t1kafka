package com.github.vitaliiev.t1kafka.consumer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@NamedEntityGraph(name = "HealthRecord.base",
		attributeNodes = {
				@NamedAttributeNode("service"), @NamedAttributeNode("details")
		}
)
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

	@ElementCollection(fetch = FetchType.EAGER)
	private Map<String, String> details;

}

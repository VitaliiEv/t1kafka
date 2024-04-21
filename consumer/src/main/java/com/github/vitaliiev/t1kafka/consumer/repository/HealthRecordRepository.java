package com.github.vitaliiev.t1kafka.consumer.repository;

import com.github.vitaliiev.t1kafka.consumer.model.HealthRecord;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, UUID> {

	@Override
	@EntityGraph("HealthRecord.base")
	Optional<HealthRecord> findById(UUID uuid);

	@Override
	@EntityGraph("HealthRecord.base")
	<S extends HealthRecord> Page<S> findAll(Example<S> example, Pageable pageable);

}

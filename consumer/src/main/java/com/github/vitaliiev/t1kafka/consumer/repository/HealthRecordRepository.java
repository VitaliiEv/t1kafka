package com.github.vitaliiev.t1kafka.consumer.repository;

import com.github.vitaliiev.t1kafka.consumer.model.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, UUID> {
}

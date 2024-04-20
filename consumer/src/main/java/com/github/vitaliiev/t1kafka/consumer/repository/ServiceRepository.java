package com.github.vitaliiev.t1kafka.consumer.repository;

import com.github.vitaliiev.t1kafka.consumer.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<Service, UUID> {

	Optional<Service> findByName(String name);
}

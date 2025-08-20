package com.braveheart.gestao_ns_api.repository;

import com.braveheart.gestao_ns_api.model.reports.AssistingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssistingEntityRepository extends JpaRepository<AssistingEntity,Long> {
}

package com.braveheart.gestao_ns_api.repository;

import com.braveheart.gestao_ns_api.model.Association;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociationRepository extends JpaRepository<Association,Long> {
}

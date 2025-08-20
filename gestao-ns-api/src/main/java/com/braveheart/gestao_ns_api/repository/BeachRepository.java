package com.braveheart.gestao_ns_api.repository;

import com.braveheart.gestao_ns_api.model.Beach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeachRepository extends JpaRepository<Beach,Long> {
}

package com.braveheart.gestao_ns_api.repository;

import com.braveheart.gestao_ns_api.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends JpaRepository<Shift,Long> {
}

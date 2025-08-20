package com.braveheart.gestao_ns_api.repository;

import com.braveheart.gestao_ns_api.model.reports.RescueReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RescueReportRepository extends JpaRepository<RescueReport,Long> {
}

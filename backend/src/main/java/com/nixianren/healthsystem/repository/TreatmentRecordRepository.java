package com.nixianren.healthsystem.repository;

import com.nixianren.healthsystem.entity.TreatmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreatmentRecordRepository extends JpaRepository<TreatmentRecord, Long> {

    List<TreatmentRecord> findByResident_IdOrderByTreatmentDateAsc(Long residentId);
}

package com.nixianren.healthsystem.repository;

import com.nixianren.healthsystem.entity.ResidentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ResidentProfileRepository extends JpaRepository<ResidentProfile, Long> {
    Optional<ResidentProfile> findByResidentId(String residentId);
}
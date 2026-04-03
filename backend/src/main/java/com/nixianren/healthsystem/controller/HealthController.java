package com.nixianren.healthsystem.controller;

import com.nixianren.healthsystem.common.Result;
import com.nixianren.healthsystem.dto.ResidentProfileDTO;
import com.nixianren.healthsystem.dto.TreatmentRecordDTO;
import com.nixianren.healthsystem.entity.ResidentProfile;
import com.nixianren.healthsystem.entity.TreatmentRecord;
import com.nixianren.healthsystem.service.HealthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HealthController {

    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @PostMapping("/residents")
    public Result<ResidentProfile> createResident(@Valid @RequestBody ResidentProfileDTO dto) {
        return Result.success(healthService.createProfile(dto));
    }

    @PostMapping("/records")
    public Result<TreatmentRecord> createRecord(@Valid @RequestBody TreatmentRecordDTO dto) {
        return Result.success(healthService.createRecord(dto));
    }

    @GetMapping("/residents/{id}/records")
    public Result<List<TreatmentRecord>> getTrendData(@PathVariable Long id) {
        return Result.success(healthService.getRecordsByResident(id));
    }

    @GetMapping("/residents/{id}")
    public Result<ResidentProfile> getResident(@PathVariable Long id) {
        return Result.success(healthService.getProfile(id));
    }

    @GetMapping("/residents")
    public Result<List<ResidentProfile>> getAllResidents() {
        return Result.success(healthService.getAllProfiles());
    }
}

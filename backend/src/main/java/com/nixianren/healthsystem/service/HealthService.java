package com.nixianren.healthsystem.service;

import com.nixianren.healthsystem.dto.ResidentProfileDTO;
import com.nixianren.healthsystem.dto.TreatmentRecordDTO;
import com.nixianren.healthsystem.entity.ResidentProfile;
import com.nixianren.healthsystem.entity.TreatmentRecord;
import com.nixianren.healthsystem.exception.ResourceNotFoundException;
import com.nixianren.healthsystem.repository.ResidentProfileRepository;
import com.nixianren.healthsystem.repository.TreatmentRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HealthService {

    private final ResidentProfileRepository profileRepo;
    private final TreatmentRecordRepository recordRepo;

    public HealthService(ResidentProfileRepository profileRepo, TreatmentRecordRepository recordRepo) {
        this.profileRepo = profileRepo;
        this.recordRepo = recordRepo;
    }

    @Transactional
    public ResidentProfile createProfile(ResidentProfileDTO dto) {
        ResidentProfile profile = new ResidentProfile();
        profile.setName(dto.getName());
        profile.setGender(dto.getGender());
        profile.setAge(dto.getAge());
        profile.setPhone(dto.getPhone());
        profile.setCommunityId(dto.getCommunityId());
        profile.setConstitutionType(dto.getConstitutionType());
        profile.setMainComplaint(dto.getMainComplaint());
        profile.setInitialPainScore(dto.getInitialPainScore());
        return profileRepo.save(profile);
    }

    @Transactional
    public TreatmentRecord createRecord(TreatmentRecordDTO dto) {
        ResidentProfile resident = profileRepo.findById(dto.getResidentId())
                .orElseThrow(() -> new ResourceNotFoundException("居民不存在，ID: " + dto.getResidentId()));

        TreatmentRecord record = new TreatmentRecord();
        record.setResident(resident);
        record.setTreatmentDate(dto.getTreatmentDate());
        record.setCurrentPainScore(dto.getCurrentPainScore());
        record.setSleepChange(dto.getSleepChange());
        record.setOperatorName(dto.getOperatorName());

        return recordRepo.save(record);
    }

    public List<TreatmentRecord> getRecordsByResident(Long residentId) {
        return recordRepo.findByResident_IdOrderByTreatmentDateAsc(residentId);
    }

    public ResidentProfile getProfile(Long id) {
        return profileRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("居民不存在，ID: " + id));
    }

    public List<ResidentProfile> getAllProfiles() {
        return profileRepo.findAll();
    }
}

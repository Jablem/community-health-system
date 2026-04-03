package com.nixianren.healthsystem.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TreatmentRecordDTO {

    @NotNull(message = "居民ID不能为空")
    private Long residentId;

    @NotNull(message = "调理日期不能为空")
    private LocalDate treatmentDate;

    @Min(value = 0, message = "疼痛评分不能为负数")
    @Max(value = 10, message = "疼痛评分不能超过10")
    private Double currentPainScore;

    @Min(value = 1, message = "睡眠变化必须在1-4之间")
    @Max(value = 4, message = "睡眠变化必须在1-4之间")
    private Integer sleepChange;

    private String operatorName;
}

package com.nixianren.healthsystem.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResidentProfileDTO {

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotNull(message = "性别不能为空")
    @Min(value = 1, message = "性别只能为1或2")
    @Max(value = 2, message = "性别只能为1或2")
    private Integer gender;

    @NotNull(message = "年龄不能为空")
    @Min(value = 0, message = "年龄不能为负数")
    @Max(value = 150, message = "年龄不能超过150")
    private Integer age;

    private String phone;

    @NotNull(message = "社区ID不能为空")
    private Long communityId;

    @Min(value = 1, message = "体质类型必须在1-9之间")
    @Max(value = 9, message = "体质类型必须在1-9之间")
    private Integer constitutionType;

    private String mainComplaint;

    @Min(value = 0, message = "疼痛评分不能为负数")
    @Max(value = 10, message = "疼痛评分不能超过10")
    private Double initialPainScore;
}

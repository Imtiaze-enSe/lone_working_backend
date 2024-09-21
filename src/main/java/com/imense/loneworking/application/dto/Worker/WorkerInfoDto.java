package com.imense.loneworking.application.dto.Worker;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class WorkerInfoDto {
    private Long id;
    private String profile_photo;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime created_at;

    private String function;
    private String address;
    private String company_logo;  // + company name + site id
    private String site_name;
    private String department;
    private String contact_person_phone;
    private String contact_person;
    private String report_to;
    // health info ------- // remove
    private String blood_type;
    private String diseases;
    private String medications;
    private Boolean alcoholic;
    private Boolean smoking;
}

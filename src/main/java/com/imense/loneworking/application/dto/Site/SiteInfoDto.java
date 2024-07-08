package com.imense.loneworking.application.dto.Site;

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
public class SiteInfoDto {
    private Long site_id;
    private String SiteName;
    private String companyName;
    private String location;
    private Integer nbrZones;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime siteCreatedAt;
}

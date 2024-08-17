package com.imense.loneworking.application.dto.Worker;


import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
public class NearbyWorkersDto {
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
}

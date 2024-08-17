package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.Worker.LocationUpdateDto;
import com.imense.loneworking.application.dto.Worker.NearbyWorkersDto;

import java.util.List;

public interface LocationService {
    void processLocationUpdate(LocationUpdateDto locationUpdate);
}

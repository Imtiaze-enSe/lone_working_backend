package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Update.UpdateCreationDto;
import com.imense.loneworking.application.dto.Update.UpdateInfoDto;
import com.imense.loneworking.application.service.serviceInterface.UpdateService;
import com.imense.loneworking.domain.entity.Alert;
import com.imense.loneworking.domain.entity.Update;
import com.imense.loneworking.domain.repository.AlertRepository;
import com.imense.loneworking.domain.repository.UpdateRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UpdateServiceImpl implements UpdateService {
    private final UpdateRepository updateRepository;
    private final AlertRepository alertRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public UpdateServiceImpl(UpdateRepository updateRepository,AlertRepository alertRepository,SimpMessagingTemplate simpMessagingTemplate) {
        this.updateRepository = updateRepository;
        this.alertRepository=alertRepository;
        this.simpMessagingTemplate =simpMessagingTemplate;
    }

    @Override
    public void sendUpdate(UpdateCreationDto updateCreationDto) {
        Optional<Alert> alert=alertRepository.findById(updateCreationDto.getAlert_id());

        if(alert.isPresent()){
            Update update=new Update();
            update.setAlert(alert.get());
            update.setTitle(updateCreationDto.getTitle());
            update.setMessage(updateCreationDto.getMessage());

            Update savedUpdate=updateRepository.save(update);
            UpdateInfoDto updateInfoDto=new UpdateInfoDto();
            updateInfoDto.setTitle(savedUpdate.getTitle());
            updateInfoDto.setMessage(savedUpdate.getMessage());
            updateInfoDto.setAlert_id(savedUpdate.getAlert().getId_alert());
            updateInfoDto.setUpdate_id(savedUpdate.getId_update());
            updateInfoDto.setUpdate_created_at(savedUpdate.getUpdate_created_at());


            simpMessagingTemplate.convertAndSend(
                    "/topic/updates/alert/" + update.getAlert().getId_alert(),
                    updateInfoDto
            );

        }

    }


    @Override
    public List<UpdateInfoDto> getUpdates(Long alert_id) {
        Optional<Alert> alertOpt=alertRepository.findById(alert_id);
        List<UpdateInfoDto> updateInfoDtos=new ArrayList<>();
        if(alertOpt.isPresent()) {
            Alert alert = alertOpt.get();
            List<Update> updates =  alert.getUpdates();
            for (Update update:updates){
                UpdateInfoDto updateInfoDto=new UpdateInfoDto();
                updateInfoDto.setTitle(update.getTitle());
                updateInfoDto.setMessage(update.getMessage());
                updateInfoDto.setUpdate_created_at(update.getUpdate_created_at());
                updateInfoDto.setUpdate_id(update.getId_update());
                updateInfoDto.setAlert_id(update.getAlert().getId_alert());
                updateInfoDtos.add(updateInfoDto);

            }

        }

        return updateInfoDtos;
    }
}

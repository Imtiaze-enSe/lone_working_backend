package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Update.UpdateCreationDto;
import com.imense.loneworking.application.dto.Update.UpdateInfoDto;
import com.imense.loneworking.application.service.serviceInterface.UpdateService;
import com.imense.loneworking.domain.entity.Alert;
import com.imense.loneworking.domain.entity.Update;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.repository.AlertRepository;
import com.imense.loneworking.domain.repository.UpdateRepository;
import com.imense.loneworking.domain.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UpdateServiceImpl implements UpdateService {
    private final UpdateRepository updateRepository;
    private final AlertRepository alertRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserRepository userRepository;

    public UpdateServiceImpl(UpdateRepository updateRepository, AlertRepository alertRepository, SimpMessagingTemplate simpMessagingTemplate, UserRepository userRepository) {
        this.updateRepository = updateRepository;
        this.alertRepository=alertRepository;
        this.simpMessagingTemplate =simpMessagingTemplate;
        this.userRepository = userRepository;
    }

    @Override
    public void sendUpdate(UpdateCreationDto updateCreationDto) {
        Optional<Alert> alert=alertRepository.findById(updateCreationDto.getAlert_id());

        if(alert.isPresent()){
            if(Objects.equals(updateCreationDto.getTitle(), "Task Duration extended")){
                alert.get().setDuration(alert.get().getDuration() + updateCreationDto.getExtended_duration());
                alert.ifPresent(alertRepository::save);
            }
            if (Objects.equals(updateCreationDto.getTitle(), "Alert has been closed.")){
                alert.get().setAlert_status("Closed");
                alert.ifPresent(alertRepository::save);
            }
            System.out.println(alert.get().getAlert_status());
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
            updateInfoDto.setExtended_duration(updateCreationDto.getExtended_duration());
            System.out.println(updateInfoDto);
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

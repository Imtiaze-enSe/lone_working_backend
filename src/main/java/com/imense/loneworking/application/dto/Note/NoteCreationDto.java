package com.imense.loneworking.application.dto.Note;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class NoteCreationDto {
    private Long alert_id;
    private String note;
}

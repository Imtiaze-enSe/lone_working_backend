package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.Note.NoteCreationDto;
import com.imense.loneworking.application.dto.Note.NoteInfoDto;
import com.imense.loneworking.application.dto.Note.NoteUpdateDto;
import com.imense.loneworking.domain.entity.Note;

import java.util.List;

public interface NoteService {
    Note addNote(NoteCreationDto noteCreationDto);

    Note updateNote(Long note_id, NoteUpdateDto noteUpdateDto);
    List<NoteInfoDto> getNoteByAlert(Long alert_id);

    void deleteNote(Long note_id);
}

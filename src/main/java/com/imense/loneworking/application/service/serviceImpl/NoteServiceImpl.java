package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Note.NoteCreationDto;
import com.imense.loneworking.application.dto.Note.NoteInfoDto;
import com.imense.loneworking.application.dto.Note.NoteUpdateDto;
import com.imense.loneworking.application.service.serviceInterface.NoteService;
import com.imense.loneworking.domain.entity.Alert;
import com.imense.loneworking.domain.entity.Note;
import com.imense.loneworking.domain.repository.AlertRepository;
import com.imense.loneworking.domain.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final AlertRepository alertRepository;


    public NoteServiceImpl(NoteRepository noteRepository, AlertRepository alertRepository) {
        this.noteRepository = noteRepository;
        this.alertRepository = alertRepository;
    }

    @Override
    public Note addNote(NoteCreationDto noteCreationDto) {
        Optional<Alert> alertOpt=alertRepository.findById(noteCreationDto.getAlert_id());

        if(alertOpt.isPresent()) {
            Alert alert = alertOpt.get();
            Note note=new Note();
            note.setNote(noteCreationDto.getNote());
            note.setAlert(alert);
            return noteRepository.save(note);
        }
     return null;
    }

    @Override
    public Note updateNote(Long note_id, NoteUpdateDto noteUpdateDto) {
        Optional<Note> noteOpt=noteRepository.findById(note_id);

        if(noteOpt.isPresent()) {
            Note note = noteOpt.get();
            note.setNote(noteUpdateDto.getNote_message());
            return noteRepository.save(note);
        }
        return null;
    }

    @Override
    public List<NoteInfoDto> getNoteByAlert(Long alert_id) {
        Optional<Alert> alertOpt=alertRepository.findById(alert_id);
        List<NoteInfoDto> noteInfoDtos=new ArrayList<>();
        if(alertOpt.isPresent()) {
            Alert alert = alertOpt.get();
            List<Note> notes=alert.getNotes();
            for (Note note:notes){
                NoteInfoDto noteInfoDto=new NoteInfoDto();
                noteInfoDto.setAlert_id(note.getAlert().getId_alert());
                noteInfoDto.setCreatedAt(note.getNote_created_at());
                noteInfoDto.setNote_message(note.getNote());
                noteInfoDtos.add(noteInfoDto);
            }
        }
        return noteInfoDtos;
    }

    @Override
    public void deleteNote(Long note_id) {
        Optional<Note> noteOpt=noteRepository.findById(note_id);
        if(noteOpt.isPresent()) {
            Note note = noteOpt.get();
            noteRepository.delete(note);}
    }
}

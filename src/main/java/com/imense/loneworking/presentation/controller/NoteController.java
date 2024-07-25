package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Note.NoteCreationDto;
import com.imense.loneworking.application.dto.Note.NoteInfoDto;
import com.imense.loneworking.application.dto.Note.NoteUpdateDto;
import com.imense.loneworking.application.service.serviceInterface.NoteService;
import com.imense.loneworking.domain.entity.Note;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }


    @PostMapping("/web/note")
    public Note addNote(@RequestBody NoteCreationDto noteCreationDto){
        return noteService.addNote(noteCreationDto);
    }


@PutMapping("/web/note/{id}")
    public Note updateNote(@PathVariable Long id, @RequestBody NoteUpdateDto noteUpdateDto){
        return  noteService.updateNote(id,noteUpdateDto);
}

@GetMapping("/web/notes/{alert_id}")
    public List<NoteInfoDto> getNotesFroAlert(@PathVariable Long alert_id){
        return noteService.getNoteByAlert(alert_id);
}

@DeleteMapping("/web/note/{id}")
    public void deleteNote(@PathVariable Long id){
        noteService.deleteNote(id);
}

}

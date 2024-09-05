package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Note.NoteCreationDto;
import com.imense.loneworking.application.dto.Note.NoteInfoDto;
import com.imense.loneworking.application.dto.Note.NoteUpdateDto;
import com.imense.loneworking.application.service.serviceInterface.NoteService;
import com.imense.loneworking.domain.entity.Note;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // Add a new note
    @PostMapping("/web/note")
    public ResponseEntity<Note> addNote(@RequestBody NoteCreationDto noteCreationDto) {
        try {
            Note createdNote = noteService.addNote(noteCreationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);  // 201 Created
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Update an existing note
    @PutMapping("/web/note/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody NoteUpdateDto noteUpdateDto) {
        try {
            Note updatedNote = noteService.updateNote(id, noteUpdateDto);
            return ResponseEntity.ok(updatedNote);  // 200 OK
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Get all notes for a specific alert
    @GetMapping("/web/notes/{alert_id}")
    public ResponseEntity<List<NoteInfoDto>> getNotesForAlert(@PathVariable Long alert_id) {
        try {
            List<NoteInfoDto> notes = noteService.getNoteByAlert(alert_id);
            return ResponseEntity.ok(notes);  // 200 OK
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Delete a specific note
    @DeleteMapping("/web/note/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        try {
            noteService.deleteNote(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

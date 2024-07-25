package com.imense.loneworking.domain.repository;

import com.imense.loneworking.domain.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository  extends JpaRepository<Note, Long> {
}

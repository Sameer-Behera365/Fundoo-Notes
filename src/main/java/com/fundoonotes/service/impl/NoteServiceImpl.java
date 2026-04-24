package com.fundoonotes.service.impl;

import com.fundoonotes.dto.request.NoteRequestDto;
import com.fundoonotes.dto.response.NoteResponseDto;
import com.fundoonotes.entity.Note;
import com.fundoonotes.exception.NoteNotFoundException;
import com.fundoonotes.repository.NoteRepository;
import com.fundoonotes.service.NoteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public NoteResponseDto createNote(NoteRequestDto requestDto, Long userId) {
        Note note = new Note();
        note.setTitle(requestDto.getTitle());
        note.setDescription(requestDto.getDescription());
        note.setUserId(userId);
        note.setPinned(false);
        note.setArchived(false);
        note.setTrashed(false);
        Note savedNote = noteRepository.save(note);
        return mapToResponse(savedNote);
    }

    @Override
    public List<NoteResponseDto> getAllNotes(Long userId) {
        return noteRepository
                .findByUserIdAndIsArchivedFalseAndIsTrashedFalse(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NoteResponseDto togglePin(Long noteId, Long userId) {
        Note note = getNoteByIdAndUserId(noteId, userId);
        note.setPinned(!note.isPinned());
        return mapToResponse(noteRepository.save(note));
    }

    @Override
    public NoteResponseDto toggleArchive(Long noteId, Long userId) {
        Note note = getNoteByIdAndUserId(noteId, userId);
        note.setArchived(!note.isArchived());
        return mapToResponse(noteRepository.save(note));
    }

    @Override
    public NoteResponseDto toggleTrash(Long noteId, Long userId) {
        Note note = getNoteByIdAndUserId(noteId, userId);
        note.setTrashed(!note.isTrashed());
        return mapToResponse(noteRepository.save(note));
    }

    private Note getNoteByIdAndUserId(Long noteId, Long userId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));
        if (!note.getUserId().equals(userId)) {
            throw new NoteNotFoundException("Note not found for this user");
        }
        return note;
    }

    protected NoteResponseDto mapToResponse(Note note) {
        return new NoteResponseDto(
                note.getId(),
                note.getTitle(),
                note.getDescription(),
                note.isPinned(),
                note.isArchived(),
                note.isTrashed(),
                note.getUserId()
        );
    }
}

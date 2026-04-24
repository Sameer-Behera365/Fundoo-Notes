package com.fundoonotes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoteResponseDto {
    private Long id;
    private String title;
    private String description;
    private boolean isPinned;
    private boolean isArchived;
    private boolean isTrashed;
    private Long userId;
}

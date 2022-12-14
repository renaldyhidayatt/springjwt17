package com.sanedge.rolepermissionbaru.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorialResponseDto {
    private Long id;
    private String title;
    private String description;
    private boolean published;
    private CommentResponseDto comment;
}

package com.backend.course.backendcourseadditional.dto;

import lombok.Data;

@Data
public class PostDto {
    private Long id;
    private String text;
    private Long authorId;
}
package com.backend.course.backendcourseadditional.dto;

import lombok.Data;

@Data
public class PostLikeDto {
    private Long id;
    private Long userId;
    private Long postId;
}
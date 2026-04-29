package com.backend.course.backendcourseadditional.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelfLikeStatsDto {
    private Long userId;
    private String nickname;
    private Long selfLikes;
}
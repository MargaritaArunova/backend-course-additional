package com.backend.course.backendcourseadditional.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLikeToUserPostDto {
    private Long likeUserId;
    private Long postUserId;
}
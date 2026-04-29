package com.backend.course.backendcourseadditional.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String nickname;
    private String email;
}
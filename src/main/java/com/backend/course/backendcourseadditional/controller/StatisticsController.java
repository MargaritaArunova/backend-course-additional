package com.backend.course.backendcourseadditional.controller;

import com.backend.course.backendcourseadditional.dto.SelfLikeStatsDto;
import com.backend.course.backendcourseadditional.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/statistics/self-likes")
    public List<SelfLikeStatsDto> getSelfLikes() {
        return statisticsService.getSelfLikeStats();
    }
}

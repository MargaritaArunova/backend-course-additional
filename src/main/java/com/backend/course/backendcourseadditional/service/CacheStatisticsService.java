package com.backend.course.backendcourseadditional.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

public class CacheStatisticsService {

    final int delay;

    private final StatisticsService statisticsService;

    public CacheStatisticsService(int delay, StatisticsService statisticsService) {
        this.delay = delay;
        this.statisticsService = statisticsService;
    }

    @Async(value = "applicationTaskExecutor")
    @Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")
    public void scheduleFixedRateTaskAsync() throws InterruptedException {
        System.out.println(
                Thread.currentThread().getName() + " - Cache Size - " + statisticsService.getCacheSize());
        Thread.sleep(delay);
    }
}

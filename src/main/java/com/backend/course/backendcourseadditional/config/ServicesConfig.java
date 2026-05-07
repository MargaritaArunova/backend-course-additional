package com.backend.course.backendcourseadditional.config;

import com.backend.course.backendcourseadditional.service.CacheStatisticsService;
import com.backend.course.backendcourseadditional.service.StatisticsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfig {

    @Bean
    @ConditionalOnProperty(prefix = "statistics", name = "service", havingValue = "console10000")
    CacheStatisticsService statisticsService2000(StatisticsService statisticsService){
        return new CacheStatisticsService(10000, statisticsService);
    }

    @Bean
    @ConditionalOnProperty(prefix = "statistics", name = "service", havingValue = "console2000")
    CacheStatisticsService statisticsService1000(StatisticsService statisticsService){
        return new CacheStatisticsService(2000, statisticsService);
    }
}

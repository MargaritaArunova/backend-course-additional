package com.backend.course.backendcourseadditional.service;

import com.backend.course.backendcourseadditional.dto.PostDto;
import com.backend.course.backendcourseadditional.dto.PostLikeDto;
import com.backend.course.backendcourseadditional.dto.SelfLikeStatsDto;
import com.backend.course.backendcourseadditional.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final RestTemplate restTemplate;

    @Value("${backend.app.url}")
    private String backendAppUrl;

    public List<SelfLikeStatsDto> getSelfLikeStats() {
        // Получаем всех пользователей
        List<UserDto> users = restTemplate.exchange(
                backendAppUrl + "/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserDto>>() {}
        ).getBody();

        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }

        // Получаем все посты
        List<PostDto> posts = restTemplate.exchange(
                backendAppUrl + "/posts",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PostDto>>() {}
        ).getBody();

        if (posts == null || posts.isEmpty()) {
            return users.stream()
                    .map(user -> new SelfLikeStatsDto(user.getId(), user.getNickname(), 0L))
                    .sorted(Comparator.comparing(SelfLikeStatsDto::getUserId))
                    .collect(Collectors.toList());
        }

        // Получаем все лайки
        List<PostLikeDto> likes = restTemplate.exchange(
                backendAppUrl + "/likes",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PostLikeDto>>() {}
        ).getBody();

        if (likes == null || likes.isEmpty()) {
            return users.stream()
                    .map(user -> new SelfLikeStatsDto(user.getId(), user.getNickname(), 0L))
                    .sorted(Comparator.comparing(SelfLikeStatsDto::getUserId))
                    .collect(Collectors.toList());
        }

        // Создаем map для быстрого поиска постов по id
        Map<Long, PostDto> postMap = posts.stream()
                .collect(Collectors.toMap(PostDto::getId, post -> post));

        // Подсчитываем самолайки для каждого пользователя
        Map<Long, Long> selfLikesCount = new HashMap<>();

        for (PostLikeDto like : likes) {
            PostDto post = postMap.get(like.getPostId());
            if (post != null && post.getAuthorId().equals(like.getUserId())) {
                // Это самолайк: автор поста = пользователь который поставил лайк
                selfLikesCount.merge(like.getUserId(), 1L, Long::sum);
            }
        }

        // Формируем результат
        return users.stream()
                .map(user -> new SelfLikeStatsDto(
                        user.getId(),
                        user.getNickname(),
                        selfLikesCount.getOrDefault(user.getId(), 0L)
                ))
                .sorted(Comparator.comparing(SelfLikeStatsDto::getUserId))
                .collect(Collectors.toList());
    }
}

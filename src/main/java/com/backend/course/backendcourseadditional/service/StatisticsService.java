package com.backend.course.backendcourseadditional.service;

import com.backend.course.backendcourseadditional.dto.*;
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

    // Кеш для хранения постов
    private final Map<Long, PostDto> postCache = new HashMap<>();

    public Long getCacheSize() {
        return (long) postCache.size();
    }

    public List<SelfLikeStatsDto> getSelfLikeStats() {
        // Получаем всех пользователей
        List<UserDto> users = getAllUsers();
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }

        // Получаем все лайки
        List<PostLikeDto> likes = getAllLikes();
        List<UserLikeToUserPostDto> userLikeToUserPostDtos = likes.stream()
                .map(like -> new UserLikeToUserPostDto(
                        like.getUserId(),
                        getPost(like.getPostId()).getAuthorId()
                ))
                .toList();

        // Подсчитываем самолайки для каждого пользователя
        Map<Long, Long> selfLikesCount = new HashMap<>();
        for (UserLikeToUserPostDto likeToPost : userLikeToUserPostDtos) {
            if (likeToPost != null && likeToPost.getLikeUserId().equals(likeToPost.getPostUserId())) {
                // Это самолайк: автор поста = пользователь который поставил лайк
                selfLikesCount.merge(likeToPost.getLikeUserId(), 1L, Long::sum);
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

    private List<PostLikeDto> getAllLikes() {
        return restTemplate.exchange(
                backendAppUrl + "/likes",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PostLikeDto>>() {
                }
        ).getBody();
    }

    private List<UserDto> getAllUsers() {
        return restTemplate.exchange(
                backendAppUrl + "/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserDto>>() {
                }
        ).getBody();
    }


    private PostDto getPost(Long id) {
        // Сначала проверяем наличие поста в кеше
        if (postCache.containsKey(id)) {
            return postCache.get(id);
        }

        // Если поста нет в кеше, запрашиваем его из API
        PostDto post = restTemplate.exchange(
                backendAppUrl + "/posts/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PostDto>() {
                }
        ).getBody();

        // Сохраняем пост в кеш
        if (post != null) {
            postCache.put(id, post);
        }

        return post;
    }
}

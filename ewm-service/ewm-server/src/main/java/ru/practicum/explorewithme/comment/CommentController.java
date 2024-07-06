package ru.practicum.explorewithme.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.comment.dto.CommentDTO;
import ru.practicum.explorewithme.comment.dto.CommentOutDTO;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CommentController {
    private final CommentService service;

    @Validated
    @PostMapping("/users/{userId}/events/{eventId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentOutDTO saveComment(@Valid @RequestBody CommentDTO commentDTO,
                                     @PathVariable Integer userId,
                                     @PathVariable Integer eventId) {
        log.info("Получен запрос Post, на добавление комментария к событию: {}", eventId);
        return service.saveComment(commentDTO, userId, eventId);
    }

    @Validated
    @PatchMapping("/users/{userId}/comments/{commentId}")
    public CommentOutDTO updateComment(@Valid @RequestBody CommentDTO commentDTO,
                                       @PathVariable Integer userId,
                                       @PathVariable Integer commentId) {
        log.info("Получен запрос Patch на обновление комментария: {}", commentId);
        return service.updateComment(commentDTO, userId, commentId);
    }

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Integer userId,
                              @PathVariable Integer commentId) {
        log.info("Получен запрос DELETE на удаление комментария по id: {}", commentId);
        service.deleteComment(userId, commentId);
    }

    @DeleteMapping("/admin/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Integer commentId) {
        log.info("Получен запрос DELETE на удаление комментария по id: {}", commentId);
        service.deleteComment(commentId);
    }
}

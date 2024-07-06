package ru.practicum.explorewithme.comment;

import ru.practicum.explorewithme.comment.dto.CommentDTO;
import ru.practicum.explorewithme.comment.dto.CommentOutDTO;

public interface CommentService {
    CommentOutDTO saveComment(CommentDTO commentDTO, Integer userId, Integer eventId);

    CommentOutDTO updateComment(CommentDTO commentDTO, Integer userId, Integer commentId);

    void deleteComment(Integer userId, Integer commentId);

    void deleteComment(Integer commentId);
}

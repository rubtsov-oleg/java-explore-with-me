package ru.practicum.explorewithme.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.comment.dto.CommentDTO;
import ru.practicum.explorewithme.comment.dto.CommentOutDTO;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.user.User;
import ru.practicum.explorewithme.user.UserRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public CommentOutDTO saveComment(CommentDTO commentDTO, Integer userId, Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("Событие " + eventId + " не найдено"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь " + userId + " не найден"));
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setText(commentDTO.getText());
        comment.setCreated(LocalDateTime.now());
        comment.setEvent(event);
        return commentMapper.toOutDTO(commentRepository.save(comment));
    }

    @Override
    public CommentOutDTO updateComment(CommentDTO commentDTO, Integer userId, Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Комментарий " + commentId + " не найден"));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new NoSuchElementException("Комментарий " + commentId + " не найден");
        }
        comment.setText(commentDTO.getText());
        comment.setUpdated(LocalDateTime.now());
        return commentMapper.toOutDTO(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Integer userId, Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Комментарий " + commentId + " не найден"));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new NoSuchElementException("Комментарий " + commentId + " не найден");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
    }
}

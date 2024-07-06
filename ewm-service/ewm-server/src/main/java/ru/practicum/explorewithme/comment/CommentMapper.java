package ru.practicum.explorewithme.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.comment.dto.CommentOutDTO;
import ru.practicum.explorewithme.comment.dto.CommentShortDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "author.id", target = "author")
    CommentOutDTO toOutDTO(Comment comment);

    List<CommentShortDTO> toShortDTOList(List<Comment> comments);
}

package ru.practicum.explorewithme.user;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toModel(UserDTO userDTO);

    UserDTO toDTO(User user);

    List<UserDTO> toListDTO(List<User> userList);
}

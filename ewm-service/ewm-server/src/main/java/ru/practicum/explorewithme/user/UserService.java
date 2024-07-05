package ru.practicum.explorewithme.user;

import java.util.List;

public interface UserService {
    UserDTO getUserById(Integer userId);

    List<UserDTO> getAllUsers(Integer from, Integer size, List<Integer> ids);

    UserDTO saveUser(UserDTO userDTO);

    void deleteUser(Integer userId);
}
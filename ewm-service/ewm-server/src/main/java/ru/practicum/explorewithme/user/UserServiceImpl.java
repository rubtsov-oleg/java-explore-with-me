package ru.practicum.explorewithme.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserDTO getUserById(Integer userId) {
        return userMapper.toDTO(userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь " + userId + " не найден")));
    }

    @Override
    public List<UserDTO> getAllUsers(Integer from, Integer size, List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return userMapper.toListDTO(userRepository.findAll(PageRequest.of(from / size, size)).getContent());
        }
        return userMapper.toListDTO(
                userRepository.findAllByIdIn(ids, PageRequest.of(from / size, size)).getContent());
    }

    @Override
    @Transactional
    public UserDTO saveUser(UserDTO userDTO) {
        return userMapper.toDTO(userRepository.save(userMapper.toModel(userDTO)));
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
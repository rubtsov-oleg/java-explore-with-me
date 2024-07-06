package ru.practicum.explorewithme.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.anotation.MarkerOfCreate;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {
    private final UserService service;

    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    @Validated({MarkerOfCreate.class})
    public UserDTO saveUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("Получен запрос Post, на создание пользователя.");
        log.info("Добавлен пользователь: {}", userDTO.getName());
        return service.saveUser(userDTO);
    }

    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer userId) {
        log.info("Получен запрос DELETE на удаления пользователя по id: {}", userId);
        service.deleteUser(userId);
    }

    @GetMapping("/admin/users")
    public List<UserDTO> getAllUsers(@RequestParam(required = false) List<Integer> ids,
                                     @RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
                                     @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос GET, на получения всех пользователей.");
        List<UserDTO> userDTOList = service.getAllUsers(from, size, ids);
        log.info("Получен ответ, список пользователей, размер: {}", userDTOList.size());
        return userDTOList;
    }
}

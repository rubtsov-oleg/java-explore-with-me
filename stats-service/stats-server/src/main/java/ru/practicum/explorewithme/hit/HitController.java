package ru.practicum.explorewithme.hit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.HitDTO;
import ru.practicum.explorewithme.dto.StatsDTO;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class HitController {
    private final HitService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@RequestBody HitDTO hitDTO) {
        log.info("Получен запрос Post, на добавление информации.");
        log.info("Добавлена информация по сервису: {}", hitDTO.getApp());
        service.saveHit(hitDTO);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatsDTO> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(required = false) boolean unique) {
        log.info("Получен запрос GET, на получение статистики по посещениям");
        List<StatsDTO> statsDTOList = service.getStats(start, end, uris, unique);
        log.info("Получен ответ, список размера: {}", statsDTOList.size());
        return statsDTOList;
    }
}
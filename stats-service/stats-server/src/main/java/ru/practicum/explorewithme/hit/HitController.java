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
    public List<StatsDTO> getStats(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                                   @RequestParam List<String> uris,
                                   @RequestParam boolean unique) {
        log.info("Получен запрос GET, на получение статистики по посещениям");
        List<StatsDTO> statsDTOList = service.getStats(start, end, uris, unique);
        log.info("Получен ответ, список размера: {}", statsDTOList.size());
        return statsDTOList;
    }
}
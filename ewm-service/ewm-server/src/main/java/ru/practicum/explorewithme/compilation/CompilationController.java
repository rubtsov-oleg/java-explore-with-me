package ru.practicum.explorewithme.compilation;

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
public class CompilationController {
    private final CompilationService service;

    @PostMapping("/admin/compilations")
    @Validated({MarkerOfCreate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationOutDTO saveCompilation(@Valid @RequestBody CompilationDTO compilationDTO) {
        log.info("Получен запрос Post на создание подборки событий.");
        log.info("Добавлена подборка: {}", compilationDTO.getTitle());
        return service.saveCompilation(compilationDTO);
    }

    @DeleteMapping("/admin/compilations/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Integer comId) {
        log.info("Получен запрос DELETE на удаление события по id: {}", comId);
        service.deleteCompilation(comId);
    }

    @PatchMapping("/admin/compilations/{comId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationOutDTO updateCompilation(@PathVariable Integer comId,
                                               @Valid @RequestBody CompilationDTO compilationDTO) {
        log.info("Получен запрос PATCH на изменение события по id: {}", comId);
        return service.updateCompilation(compilationDTO, comId);
    }

    @GetMapping("/compilations/{comId}")
    public CompilationOutDTO getCompilationById(@PathVariable Integer comId) {
        log.info("Получен запрос GET на получение подборки по id: {}", comId);
        return service.getCompilationById(comId);
    }

    @GetMapping("/compilations")
    public List<CompilationOutDTO> getAllCompilations(@RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
                                                      @RequestParam(defaultValue = "10") @Positive Integer size,
                                                      @RequestParam(required = false) Boolean pinned) {
        log.info("Получен запрос GET, на получения всех подборок.");
        List<CompilationOutDTO> compilationOutDTOList = service.getAllCompilations(from, size, pinned);
        log.info("Получен ответ, список подборок, размер: {}", compilationOutDTOList.size());
        return compilationOutDTOList;
    }
}

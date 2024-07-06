package ru.practicum.explorewithme.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CategoryController {
    private final CategoryService service;

    @GetMapping("/categories/{catId}")
    public CategoryDTO getCategoryById(@PathVariable Integer catId) {
        log.info("Получен запрос GET на получение категории по id: {}", catId);
        return service.getCategoryById(catId);
    }

    @GetMapping("/categories")
    public List<CategoryDTO> getAllCategories(@RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос GET, на получения всех категорий.");
        List<CategoryDTO> categoryDTOList = service.getAllCategories(from, size);
        log.info("Получен ответ, список категорий, размер: {}", categoryDTOList.size());
        return categoryDTOList;
    }

    @Validated
    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO saveCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        log.info("Получен запрос Post, на добавление категории.");
        log.info("Добавлена категория: {}", categoryDTO.getName());
        return service.saveCategory(categoryDTO);
    }

    @Validated
    @PatchMapping("/admin/categories/{catId}")
    public CategoryDTO updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Integer catId) {
        log.info("Получен запрос Patch на обновление категории");
        CategoryDTO categoryDTO1 = service.updateCategory(catId, categoryDTO);
        log.info("Обновлена категория: {}", categoryDTO1.getName());
        return categoryDTO1;
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer catId) {
        log.info("Получен запрос DELETE на удаление категории по id: {}", catId);
        service.deleteCategory(catId);
    }
}

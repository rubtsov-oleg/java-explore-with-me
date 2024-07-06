package ru.practicum.explorewithme.category;

import java.util.List;

public interface CategoryService {
    CategoryDTO saveCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Integer catId, CategoryDTO categoryDTO);

    void deleteCategory(Integer catId);

    List<CategoryDTO> getAllCategories(Integer from, Integer size);

    CategoryDTO getCategoryById(Integer catId);
}

package ru.practicum.explorewithme.category;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toModel(CategoryDTO categoryDTO);

    CategoryDTO toDTO(Category category);

    List<Category> toListModels(List<CategoryDTO> categoryDTOList);

    List<CategoryDTO> toListDTO(List<Category> categoryList);
}

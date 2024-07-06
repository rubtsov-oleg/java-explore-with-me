package ru.practicum.explorewithme.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        return categoryMapper.toDTO(categoryRepository.save(categoryMapper.toModel(categoryDTO)));
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Integer catId, CategoryDTO categoryDTO) {
        CategoryDTO existedCategory = getCategoryById(catId);
        if (categoryDTO.getName() != null) {
            existedCategory.setName(categoryDTO.getName());
        }
        return categoryMapper.toDTO(categoryRepository.save(categoryMapper.toModel(existedCategory)));
    }

    @Override
    @Transactional
    public void deleteCategory(Integer catId) {
        categoryRepository.deleteById(catId);
    }

    @Override
    public List<CategoryDTO> getAllCategories(Integer from, Integer size) {
        return categoryMapper.toListDTO(
                categoryRepository.findAll(PageRequest.of(from / size, size)).getContent()
        );
    }

    @Override
    public CategoryDTO getCategoryById(Integer catId) {
        return categoryMapper.toDTO(categoryRepository.findById(catId)
                .orElseThrow(() -> new NoSuchElementException("Категория " + catId + " не найдена")));
    }
}

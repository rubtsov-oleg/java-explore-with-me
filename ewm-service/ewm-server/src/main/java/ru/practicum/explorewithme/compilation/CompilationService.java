package ru.practicum.explorewithme.compilation;

import java.util.List;

public interface CompilationService {
    CompilationOutDTO saveCompilation(CompilationDTO compilationDTO);

    void deleteCompilation(Integer comId);

    CompilationOutDTO updateCompilation(CompilationDTO compilationDTO, Integer comId);

    CompilationOutDTO getCompilationById(Integer comId);

    List<CompilationOutDTO> getAllCompilations(Integer from, Integer size, Boolean pinned);
}

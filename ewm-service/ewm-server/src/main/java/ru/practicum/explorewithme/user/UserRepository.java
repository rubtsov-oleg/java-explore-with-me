package ru.practicum.explorewithme.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    Page<User> findAllByIdIn(List<Integer> ids, Pageable pageable);
}

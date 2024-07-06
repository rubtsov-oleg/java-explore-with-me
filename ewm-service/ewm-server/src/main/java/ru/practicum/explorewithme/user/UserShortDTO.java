package ru.practicum.explorewithme.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserShortDTO {
    private Integer id;
    private String name;
}

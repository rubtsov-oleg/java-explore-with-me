package ru.practicum.explorewithme.category;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class CategoryDTO {
    private Integer id;

    @NotBlank(message = "name can`t be blank!")
    @Size(min = 1, max = 50, message = "Max size 50!")
    private String name;

    @JsonCreator
    public CategoryDTO(
            @JsonProperty("id") Integer id,
            @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }
}

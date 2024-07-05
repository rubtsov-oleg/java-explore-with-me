package ru.practicum.explorewithme.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.anotation.MarkerOfCreate;
import ru.practicum.explorewithme.anotation.MarkerOfUpdate;

import javax.validation.constraints.*;

@Data
@Builder
public class UserDTO {
    @Null(groups = MarkerOfCreate.class)
    @Null(groups = MarkerOfUpdate.class)
    private Integer id;
    @NotBlank(groups = MarkerOfCreate.class, message = "name not null.")
    @Size(min = 2, max = 250, message = "Min size 6, Max size 254!")
    private String name;
    @NotNull(groups = MarkerOfCreate.class, message = "email can`t null!")
    @NotBlank(groups = MarkerOfCreate.class, message = "email not null.")
    @Email(groups = MarkerOfCreate.class, message = "Incorrect Email.")
    @Size(min = 6, max = 254, message = "Min size 6, Max size 254!")
    private String email;

    @JsonCreator
    public UserDTO(
            @JsonProperty("id") Integer id,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}

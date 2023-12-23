package zangsu.selfmadeBlog.user.controller.model;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank
    private final String longinId;

    @NotBlank
    private final String password;
}

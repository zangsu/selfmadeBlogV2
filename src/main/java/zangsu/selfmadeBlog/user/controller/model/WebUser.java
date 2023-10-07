package zangsu.selfmadeBlog.user.controller.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebUser {

    @NotBlank
    private String userName;

    @NotBlank
    private String userId;

    @NotBlank
    private String password;

}



package zangsu.selfmadeBlog.user.controller.model;

import java.util.Objects;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebUser webUser = (WebUser) o;
        return Objects.equals(userName, webUser.userName) &&
                Objects.equals(userId, webUser.userId) &&
                Objects.equals(password, webUser.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, userId, password);
    }
}



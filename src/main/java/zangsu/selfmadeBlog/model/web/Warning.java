package zangsu.selfmadeBlog.model.web;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Warning {
    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Warning warning = (Warning) o;
        return Objects.equals(message, warning.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
}

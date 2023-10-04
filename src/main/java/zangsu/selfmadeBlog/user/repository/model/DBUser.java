package zangsu.selfmadeBlog.user.repository.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
@Getter @Setter
@NoArgsConstructor
public class DBUser {

    @Id
    @GeneratedValue
    @Column(name = "IDX")
    private Long idx;

    @Column(name = "NAME")
    private String userName;

    @Column(name = "ID", unique = true)
    private String userId;

    @Column(name = "PASSWORD")
    private String password;

    public DBUser(String userName, String userId, String password) {
        this.userName = userName;
        this.userId = userId;
        this.password = password;
    }

    @Override
    public String toString() {
        return "DBUser{" +
                "idx=" + idx +
                ", userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

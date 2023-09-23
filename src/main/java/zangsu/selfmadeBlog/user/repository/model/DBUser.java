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

    @Column(name = "ID")
    private String id;

    @Column(name = "PASSWORD")
    private String password;

    public DBUser(String userName, String id, String password) {
        this.userName = userName;
        this.id = id;
        this.password = password;
    }
}

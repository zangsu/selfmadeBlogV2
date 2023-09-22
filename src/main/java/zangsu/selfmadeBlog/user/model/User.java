package zangsu.selfmadeBlog.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Tables;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
@Getter @Setter
@NoArgsConstructor
public class User {

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

    public User(String userName, String id, String password) {
        this.userName = userName;
        this.id = id;
        this.password = password;
    }
}

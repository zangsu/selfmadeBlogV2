package zangsu.selfmadeBlog.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zangsu.selfmadeBlog.user.repository.model.DBUser;

import java.util.Optional;

public interface UserDAO extends JpaRepository<DBUser, Long> {
    //public Optional<DBUser> findByUserId(String userId);

    Optional<DBUser> findDBUserByUserId(String userId);
    public boolean existsByUserId(String userId);
}

package zangsu.selfmadeBlog.user.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import zangsu.selfmadeBlog.user.repository.model.DBUser;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class tempUserDao {
    @PersistenceContext
    EntityManager em;

    @Transactional
    public void save(DBUser user) {
        em.persist(user);

    }
}

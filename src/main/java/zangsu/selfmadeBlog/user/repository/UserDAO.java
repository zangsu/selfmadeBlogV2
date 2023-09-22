package zangsu.selfmadeBlog.user.repository;

import org.springframework.stereotype.Repository;
import zangsu.selfmadeBlog.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDAO {

    @PersistenceContext
    private EntityManager em;

    /**
     * 유저를 저장
     * @param user 저장할 유저 엔티티
     * @return 저장된 유저의 idx 값
     */
    public long save(User user){
        em.persist(user);
        return user.getIdx();
    }

    /**
     * 유저의 idx 값을 이용해 유저를 단건 조회
     * @param idx 조회에 사용하기 위한 유저 인덱스
     * @return user 조회된 유저 / idx의 유저가 없으면 @null
     */
    public User find(long idx){
        return em.find(User.class, idx);
    }

    //update는 영속성 컨텍스트가 유저를 관리하고 있다면 자동으로 더티체크를 진행해 주니 별도의 함수가 필요하진 않을 듯 하다.

    public void delete(long idx){
        User removeUser = em.find(User.class, idx);
        em.remove(removeUser);
    }
}

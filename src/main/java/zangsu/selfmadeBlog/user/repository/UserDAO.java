package zangsu.selfmadeBlog.user.repository;

import org.springframework.stereotype.Repository;
import zangsu.selfmadeBlog.user.exception.CantModifyFieldException;
import zangsu.selfmadeBlog.user.repository.model.DBUser;
import zangsu.selfmadeBlog.user.exception.NoSuchUserException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserDAO {

    @PersistenceContext
    private EntityManager em;

    /**
     * 유저를 저장
     * @param user 저장할 유저 엔티티
     * @return 저장된 유저의 idx 값
     */
    public long save(DBUser user){
        em.persist(user);
        return user.getIdx();
    }

    /**
     * 유저의 idx 값을 이용해 유저를 단건 조회
     * @param idx 조회에 사용하기 위한 유저 인덱스
     * @return user 조회된 유저 / idx의 유저가 없으면 @null
     * @throws NoSuchUserException idx 인덱스를 가지는 유저가 존재하지 않는 경우
     */
    public DBUser find(long idx) throws NoSuchUserException {
        DBUser findUser = em.find(DBUser.class, idx);
        if(findUser == null)
            throw new NoSuchUserException(idx + " 유저가 존재하지 않습니다.");
        return findUser;
    }

    /**
     * 유저 수정 기능
     * @param idx 수정할 유저의 idx 값
     * @param user 수정할 유저의 데이터를 담고 있는 객체
     * @return id를 수정하려 하면 false, 그렇지 않은 경우 수정을 완료한 뒤 true
     * @throws NoSuchUserException idx 인덱스를 가지는 유저가 존재하지 않는 경우
     */
    public void modify(long idx, DBUser user) throws NoSuchUserException, CantModifyFieldException{
        DBUser originalUser = this.find(idx);

        if(!originalUser.getId().equals(user.getId()))
            throw new CantModifyFieldException("유저의 ID 값은 변경될 수 없습니다.");

        originalUser.setUserName(user.getUserName());
        originalUser.setPassword(user.getPassword());
    }

    public void delete(long idx) throws NoSuchUserException{
        DBUser removeUser = em.find(DBUser.class, idx);
        em.remove(removeUser);
    }
}

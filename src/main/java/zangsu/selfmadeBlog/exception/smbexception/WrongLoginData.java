package zangsu.selfmadeBlog.exception.smbexception;

import zangsu.selfmadeBlog.exception.SmbException;

public class WrongLoginData extends SmbException {

    public static final String exceptionMessage = "아이디 또는 비밀번호가 잘못되었습니다.";

    public WrongLoginData() {
        super(exceptionMessage);
    }
}

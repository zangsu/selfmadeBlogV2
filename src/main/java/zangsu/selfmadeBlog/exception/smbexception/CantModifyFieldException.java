package zangsu.selfmadeBlog.exception.smbexception;

import lombok.Getter;
import zangsu.selfmadeBlog.exception.SmbException;


public class CantModifyFieldException extends SmbException {

    public static final String exceptionMessage = "ID는 수정할 수 없습니다.";
    public CantModifyFieldException() {
        super(exceptionMessage);
    }
}

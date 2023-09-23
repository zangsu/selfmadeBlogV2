package zangsu.selfmadeBlog.user.exception;

public class CantModifyFieldException extends IllegalStateException{
    public CantModifyFieldException(String s) {
        super(s);
    }
}

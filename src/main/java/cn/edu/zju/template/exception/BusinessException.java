package cn.edu.zju.template.exception;

public class BusinessException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BusinessException(int code, String msg) {
        super(code, msg);
    }

    public BusinessException(int code, String msg, Throwable e) {
        super(code, msg, e);
    }
}

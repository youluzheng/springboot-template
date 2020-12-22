package cn.edu.zju.template.exception;

public class ParamFormatException extends BaseException {
    private static final long serialVersionUID = 1L;

    public ParamFormatException(int code, String msg) {
        super(code, msg);
    }

    public ParamFormatException(int code, String msg, Throwable e) {
        super(code, msg, e);
    }
}

package cn.edu.zju.template.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "not found")
public class BaseException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int errorCode;

    public BaseException(int code, String msg) {
        super(msg);
        this.errorCode = code;
    }

    public BaseException(int code, String msg, Throwable e) {
        super(msg, e);
        this.errorCode = code;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}

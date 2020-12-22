package cn.edu.zju.template.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cn.edu.zju.template.exception.BaseException;
import cn.edu.zju.template.util.ResponseUtil;


/**
 * @author zyl
 * 全局异常捕获
 */
@RestControllerAdvice
public class GlobalExceptionHandle {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    @ExceptionHandler
    public ResponseUtil BaseExceptionHandler(BaseException e) {
        log.error("error return : {}", e.getMessage());
        return new ResponseUtil(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler
    public ResponseUtil ThrowableExceptionHandler(Throwable e) {
        log.error(e.getMessage(), e);
        return new ResponseUtil(500, "系统无法处理该请求");
    }
}

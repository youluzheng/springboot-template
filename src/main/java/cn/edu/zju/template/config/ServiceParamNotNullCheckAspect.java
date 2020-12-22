package cn.edu.zju.template.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author zyl
 * service层方法参数NULL检查 <br>
 * 使用<code>Objects.requireNonNull(obj)<code>方法
 */
@Component
@Aspect
public class ServiceParamNotNullCheckAspect {
    public static final Logger log = LoggerFactory.getLogger(ControllerLogAspect.class);

    /**
     * 切面点
     */
    private final String POINT_CUT = "execution(* cn.edu.zju.train.service.*.*(..))";

    @Pointcut(POINT_CUT)
    private void pointcut() {
    }

    /**
     * 前置通知，方法调用前被调用
     *
     * @param joinPoint
     */
    @Before(value = POINT_CUT)
    public void before(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames(); // 参数名
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        //参数注解，1维是参数，2维是注解
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            Annotation[] paramAnn = annotations[i];
            boolean flag = false;
            // 查找参数是否含有Nullable注解
            for (Annotation annotation : paramAnn) {
                if (annotation.annotationType().equals(Nullable.class)) {
                    flag = true;
                    break;
                }
            }
            // 如果没有Nullable则参数不能为null
            if (flag == false) {
                Objects.requireNonNull(args[i], methodName + "要求参数:" + argNames[i] + "NOT NULL");
            }
        }
    }
}

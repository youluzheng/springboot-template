package cn.edu.zju.template.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zyl
 * 表示方法或类中全部方法不需要token验证
 * 如果方法或者类中都没有注解，默认为需要token验证
 * 方法中的注解优先级高于类中的注解，即若方法和类中的注解冲突，以方法中的为主
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenNotRequire {

}

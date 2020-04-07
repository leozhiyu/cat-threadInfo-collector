package com.weimob.cat.annotation;

import com.weimob.cat.executor.CatThreadInfoStarter;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * @author yaocai.li
 * @date 2020/4/4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({CatThreadInfoStarter.class})
public @interface EnableThreadCatCollector {

}

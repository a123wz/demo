package com.zywl.opendubbbo;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Configuration
@Import(DubboClientsRegistrar.class)
public @interface EnableDubboClients {

    String[] value() default {};
}

package com.market.carrot.config;

import com.market.carrot.login.domain.Role;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

  long userId() default 1;

  String username() default "user";

  Role role() default Role.USER;
}

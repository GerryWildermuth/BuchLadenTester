package com.Tester.BuchLadenTester.Helper;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String username() default "tester";

    String role() default "ADMIN";

    String password() default "password";

    String name() default "Tester bob";
}
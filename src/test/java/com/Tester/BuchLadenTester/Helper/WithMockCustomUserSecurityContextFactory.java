package com.Tester.BuchLadenTester.Helper;

import com.Tester.BuchLadenTester.Model.CustomUserDetails;
import com.Tester.BuchLadenTester.Model.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        CustomUserDetails principal =
                new CustomUserDetails(customUser.name(), customUser.username());
        principal.setEmail(customUser.username());
        principal.addRoleUser(new Role(customUser.role(), customUser.role()));
        principal.setPassword(customUser.password());
        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
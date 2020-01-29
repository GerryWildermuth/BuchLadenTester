package com.Tester.BuchLadenTester.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static com.Tester.BuchLadenTester.BuchLadenTesterApplication.logger;

@Service
public class SecurityServiceImpl implements SecurityService{

    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImpl UserDetailsService;

    public SecurityServiceImpl(AuthenticationManager authenticationManager, UserDetailsServiceImpl UserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.UserDetailsService = UserDetailsService;
    }

    @Override
    public String findLoggedInUsername() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (userDetails instanceof UserDetails) {
            return ((UserDetails)userDetails).getUsername();
        }

        return null;
    }

    @Override
    public void autoLogin(String username, String password) {
        UserDetails userDetails = UserDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            logger.debug("Auto login was successfully!"+ username);
        }
    }
}
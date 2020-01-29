package com.Tester.BuchLadenTester.Service;

import com.Tester.BuchLadenTester.Model.Role;
import com.Tester.BuchLadenTester.Model.User;
import com.Tester.BuchLadenTester.Repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Service("UserDetailsService")
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;

    private final HttpServletRequest request;

    public UserDetailsServiceImpl(UserRepository userRepository, HttpServletRequest request) {
        super();
        this.userRepository = userRepository;
        this.request = request;
    }

    // Muss vorhanden und richtig Implementiert sein
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final String ip = getClientIP();

        try {
            final User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException("No user found with username: " + email);
            }
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            for (Role role : user.getUserRoles()){
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));//error
            }

            UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
            return  userDetails;
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

}
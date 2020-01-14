package com.Tester.BuchLadenTester.Service;

import com.Tester.BuchLadenTester.Repository.UserRepository;
import com.Tester.BuchLadenTester.Model.Role;
import com.Tester.BuchLadenTester.Model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
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
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    private final HttpServletRequest request;

    public UserDetailService(UserRepository userRepository, HttpServletRequest request) {
        super();
        this.userRepository = userRepository;
        this.request = request;
    }

    // API


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
                for (Role role : user.getRoles()){
                    grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
                }

                UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
                return  userDetails;
            }
            catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
    // UTIL
    /*
    private final Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private final List<String> getPrivileges(final Collection<Role> roles) {
        final List<String> privileges = new ArrayList<String>();
        final List<Privilege> collection = new ArrayList<Privilege>();
        for (final Role role : roles) {
            collection.addAll(role.getPrivileges());
        }
        for (final Privilege item : collection) {
            privileges.add(item.getName());
        }

        return privileges;
    }

    private final List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (final String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }*/

    private final String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

}

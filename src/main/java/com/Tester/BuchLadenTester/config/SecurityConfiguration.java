package com.Tester.BuchLadenTester.config;

import com.Tester.BuchLadenTester.Service.UserDetailsServiceImpl;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.Tester.BuchLadenTester.BuchLadenTesterApplication.bCryptPasswordEncoder;

//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
//(debug = true)
@EnableSwagger2
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private MySimpleUrlAuthenticationSuccessHandler mySimpleUrlAuthenticationSuccessHandler;
    private UserDetailsServiceImpl userDetailsService;

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MySimpleUrlAuthenticationSuccessHandler();
    }
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                // URLs matching for access rights
                .antMatchers("/").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/books").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/home/**").hasAnyAuthority("SUPER_USER", "ADMIN", "USER")
                .antMatchers("/shoppingcart/**").hasAnyAuthority("SUPER_USER", "ADMIN", "USER")
                .antMatchers("/books/**").hasAnyAuthority("SUPER_USER", "ADMIN")
                .antMatchers("/authors/**").hasAnyAuthority("SUPER_USER", "ADMIN")
                .antMatchers("/admin/**").hasAnyAuthority("SUPER_USER","ADMIN")
                .anyRequest().authenticated()
                .and()
                // form login
                .csrf().disable().formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/books")
                .failureUrl("/login?error=true")
                .successHandler(myAuthenticationSuccessHandler())
                .and()
                // logout
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied");
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/static/**", "/static/css/**", "/static/js/**", "/images/**");
    }
    @Bean
    public LayoutDialect LayoutDialect()
    {
        return new LayoutDialect();
    }

}

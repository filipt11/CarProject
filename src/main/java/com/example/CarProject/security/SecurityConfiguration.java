package com.example.CarProject.security;


import com.example.CarProject.entities.MyUser;
import com.example.CarProject.repositories.MyUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final MyUserRepository myUserRepository;

    public SecurityConfiguration(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            MyUser user = myUserRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            List<GrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority(user.getRole())
            );

            return new SignedUserDetails(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    authorities,
                    user.isBanned()
            );
        };
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/authentication/**","/registrationForm","/registryUser","/registration/**").not().authenticated()
                        .requestMatchers("/","/carList","/errorPage","/accessDenied").permitAll()
                        .requestMatchers("/output.css","/flowbite.min.js","/images/**").permitAll()
                        .requestMatchers("/administration/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/authentication/login")
                        .loginProcessingUrl("/authentication/login/process")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .failureHandler((request, response, exception) -> {
                            if (exception instanceof LockedException) {
                                response.sendRedirect("/authentication/login?banned");
                            } else if (exception instanceof BadCredentialsException) {
                                response.sendRedirect("/authentication/login?failed");
                            } else {
                                response.sendRedirect("/authentication/login?failed");
                            }
                        })
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout((logout)-> logout
                                .deleteCookies("JSESSIONID","remember-me")
                                .invalidateHttpSession(true)
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                                .permitAll()
                )
                .rememberMe(remember -> remember
                        .userDetailsService(userDetailsService())
                        .tokenValiditySeconds(60*60*24*31)
                        .key("LouMCbDYl7O2s6pM8RNeJNbr379yNHrM")
                        .rememberMeParameter("remember")
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/accessDenied"));

        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

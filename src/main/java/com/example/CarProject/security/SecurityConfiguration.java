package com.example.CarProject.security;


import com.example.CarProject.repositories.MyUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final MyUserRepository myUserRepository;

    public SecurityConfiguration(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new MyDatabaseUserDetailsService(myUserRepository);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/authentication/**","/registrationForm","/registryUser","/registration/**").permitAll()
                        .requestMatchers("/","/carList","/errorPage").permitAll()
                        .requestMatchers("/output.css","/flowbite.min.js","/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((formLogin)->
                        formLogin
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .loginPage("/authentication/login")
                                .failureUrl("/authentication/login?failed")
                                .defaultSuccessUrl("/")
                                .loginProcessingUrl("/authentication/login/process")
                                .permitAll()
                )
                .logout((logout)->
                        logout.deleteCookies("JSESSIONID")
                                .invalidateHttpSession(true)
                                .logoutUrl("/customLogout")
                                .logoutSuccessUrl("/")
                                .permitAll()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

package com.exercise.UserDB.security;

import com.exercise.UserDB.rolesvalidator.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private final ValidatorFactory validatorFactory;

    public SecurityConfig(ValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.eraseCredentials(false);
    }

    @Value("${login.user1.username}")
    private String user1Username;

    @Value("${login.user1.password}")
    private String user1Password;

    @Value("${login.user2.username}")
    private String user2Username;

    @Value("${login.user2.password}")
    private String user2Password;

    @Value("${login.user3.username}")
    private String user3Username;

    @Value("${login.user3.password}")
    private String user3Password;

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails admin = User.builder()
                .username(user1Username)
                .password(passwordEncoder().encode(user1Password))
                .roles(Role.ADMIN.name())
                .build();

        UserDetails alex = User.builder()
                .username(user2Username)
                .password(passwordEncoder().encode(user2Password))
                .roles(Role.USER.name())
                .build();

        UserDetails mihai = User.builder()
                .username(user3Username)
                .password(passwordEncoder().encode(user3Password))
                .roles(Role.USER.name())
                .build();

        return new InMemoryUserDetailsManager(admin, alex, mihai);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/**")
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/stats").permitAll() 
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

package com.bandi.mhProject.config;

import com.bandi.mhProject.component.JwtTokenProvider;
import com.bandi.mhProject.config.auth.JwtAuthenticationFilter;
import com.bandi.mhProject.service.UserService;
import com.bandi.mhProject.serviceimpl.UserServiceImpl;
import com.bandi.mhProject.util.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
//    @Autowired private UserServiceImpl impl;
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers("/user/login", "/user/signin");
//    }
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .sessionManagement((sessionM) ->
                        sessionM.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //JWT를 사용하는 경우 세션이용안함. 구현예정
                .authorizeHttpRequests((auth) ->
                        auth
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .anyRequest().permitAll()
                )
//                .formLogin((formLogin) -> formLogin
////                        .usernameParameter("id")
////                        .passwordParameter("pw")
//                        .loginPage("/api/login")
//                        .loginProcessingUrl("/loginProc")
//                        .defaultSuccessUrl("/")
//                )
                .formLogin(AbstractHttpConfigurer::disable) //Spring Security 기본 fromLogin 사용안함
                .httpBasic(AbstractHttpConfigurer::disable) //Spring Security 기본 httpBasic 사용안함
//                .addFilter(new JwtAuthenticationFilter(jwtTokenProvider))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)//보안 필터 이전에 추가해 인증이나 권한부여 전 추가로직 수행
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("user")
//                .password(bCryptPasswordEncoder.encode("userPass"))
//                .roles("USER")
//                .build());
//        manager.createUser(User.withUsername("admin")
//                .password(bCryptPasswordEncoder.encode("adminPass"))
//                .roles("USER", "ADMIN")
//                .build());
//        return manager;
//    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

//    @Bean
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        System.out.println("configure");
//        auth.userDetailsService(impl);
//    }
//
//    @Bean
//    public Encoder encoder(PasswordEncoder ec){
//        return new Encoder(ec);
//    }
}

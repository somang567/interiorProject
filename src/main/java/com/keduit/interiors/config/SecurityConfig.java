package com.keduit.interiors.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 로그인 설정
        http.formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/");

        // 권한 설정
        http.authorizeRequests()
                .mvcMatchers("/", "/members/**", "/board/list/**", "/board/view/**", "/img/**", "/files/**", "/megazines/list/**")
                .permitAll() // 비로그인 사용자 접근 가능 경로

                // 로그인한 사용자만 접근 가능 경로로 설정
                .mvcMatchers("/board/write/**", "/board/writedo/**", "/board/modify/**", "/board/delete/**", "/board/update/**", "/board/{boardId}/comment/**")
                .hasRole("USER") // 로그인한 사용자만 접근 가능 경로

                .mvcMatchers("/item/addItem/**", "/item/updateItem/**", "/item/deleteItem/**", "/cs/write", "/cs/edit/**", "/cs/delete/**")
                .hasRole("ADMIN") // 관리자만 접근 가능 경로
                .anyRequest().authenticated();  // 그 외의 모든 요청은 인증 필요

        // 인증 및 인가 관련 예외 처리 설정
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return null;
    }
}

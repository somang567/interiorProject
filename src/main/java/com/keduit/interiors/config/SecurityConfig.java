package com.keduit.interiors.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity(prePostEnabled = true)
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
                // 1. 관리자 권한이 필요한 URL 패턴
                .mvcMatchers(
                        "/item/addItem/**",
                        "/item/updateItem/**",
                        "/item/deleteItem/**",
                        "/cs/edit/**",
                        "/cs/delete/**",
                        "/board/delete/**",
                        "/board/update/**",
                        "/board/modify/**",
                        "/selfinterior/delete/**",
                        "/selfinterior/update/**",
                        "/selfinterior/modify/**"
                ).hasRole("ADMIN")
                // 2. 모든 사용자가 접근 가능한 URL 패턴 (로그인 불필요)
                .mvcMatchers(
                        "/",
                        "/members/**",
                        "/board/list/**",
                        "/board/write/**",
                        "/board/view/**",
                        "/board/writedo/**",
                        "/selfinterior/list/**",      // 셀프 인테리어 게시글 목록 접근 허용
                        "/selfinterior/view/**",      // 셀프 인테리어 게시글 조회 접근 허용
                        "/img/**",
                        "/error/**",
                        "/favicon.ico",
                        "/boards/list/**",
                        "/files/**",
                        "/megazines/list/**",
                        "/item/productMain",
                        "/item/tile/**",
                        "/item/floor/**",
                        "/item/furniture/**",
                        "/item/stock/**",
                        "/item/wall/**",
                        "/item/**",
                        "/uploads/**",
                        "/cs/list/**",
                        "/cs/view/**",
                        "/cs/write"
                ).permitAll()
                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated();

        // 예외 처리
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

package com.keduit.interiors.config;
//
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
        .mvcMatchers("/", "/members/new", "/members/login", "/board/list/**", "/board/write/**", "/board/view/**", "/board/writedo/**",
            "/board/modify/**", "/board/delete/**", "/board/update/**", "/img/**", "/board/{boardId}/comment/**",
            "error", "favicon.ico", "/boards/list/**", "/files/**",
            "megazines/list/**",
            "/item/productMain", "/item/tile/**", "/item/floor/**", "/item/furniture/**", "/item/stock/**", "/item/wall/**" , "/item/**" , "/uploads/**" ,
            "/cs/list/**" , "/cs/view/**").permitAll()
        .mvcMatchers("/members/edit/**", "/members/withdraw").authenticated() // 인증된 사용자만 접근 가능
        .mvcMatchers("/megazines/edit/**","/megacomment/save/**").hasRole("USER")  // hasRole 사용
        .mvcMatchers("/item/addItem/**", "/item/updateItem/**", "/item/deleteItem/**" , "/cs/write", "/cs/edit/**", "/cs/delete/**").hasRole("ADMIN")  // hasRole 사용
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

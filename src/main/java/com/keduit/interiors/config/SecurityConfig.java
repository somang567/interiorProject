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


//passwordEncoder에서 걸려서 만들어줌..
//@Configuration: 이 클래스가 스프링의 설정 클래스를 나타내며, 스프링 컨테이너에 의해 관리되는 빈(Bean)을 정의합니다.
@Configuration
//@EnableWebSecurity: 웹 애플리케이션에 대한 보안 기능을 활성화합니다. 이 어노테이션이 있으면 스프링 시큐리티가 웹 보안을 설정할 수 있도록 합니다.
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    //로그인은 따로 페이지를 띄우지 않음.
    System.out.println("------------SecurityFilterChain 이랑께");
    http.formLogin()
        .loginPage("/members/login")
        .defaultSuccessUrl("/")
        .usernameParameter("email")
        .failureUrl("/members/login/error")
        .and()
        .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
        .logoutSuccessUrl("/");


    http.authorizeRequests()
        .mvcMatchers("/", "/members/**",
            "/img/**", "error", "favicon.ico", "/boards/list/**","/error").permitAll()
            .antMatchers("/megazines/user/write/new").hasRole("USER");
        //.anyRequest().authenticated(); //이 메서드는 위에서 정의한 특정 URL 패턴 이외의 모든 요청에 대해 인증을 요구합니다. 즉, 사용자가 인증된 상태여야만 다른 모든 요청을 수행할 수 있습니다.
    //메서드는 인증 및 인가 관련 예외를 처리하기 위한 설정을 시작하는 메서드입니다. 이 메서드를 호출하면, 인증 실패나 인가 실패 시의 동작을 정의할 수 있는 체인으로 이동합니다.
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


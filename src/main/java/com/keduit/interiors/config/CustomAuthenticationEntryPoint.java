package com.keduit.interiors.config;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Spring Security에서 인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때의 동작을 정의하는 사용자 정의 클래스
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  //네트워크 요청은 비동긔~~
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    //  ajax 비동기 통신의 경우 http request header XMLHTTPRequest 값을 넣어줌.
    //  이때 인증되지 않은 사용자가 ajax로 리소스를 요청하면 "UnAuthorized(401 에러)" 에러를 발생 시킴.

    //AuthenticationException: 이 예외는 사용자가 잘못된 자격 증명을 제공하거나, 인증 서버에서 사용자의 인증을 거부했을 때 발생합니다. 예를 들어, 잘못된 사용자 이름이나 비밀번호를 입력한 경우입니다.
    //SpringSecurityException을 상속.
    //요청 헤더: 클라이언트와 서버 간의 통신에 중요한 역할을 하는 칑긔..종류 한 5가지가 있음.
    //요청 헤더에서 x-requested-with 값을 확인하여 AJAX 요청인지 판별합니다. AJAX 요청인 경우, 클라이언트는 비동기 방식으로 데이터를 요청하고 있기 때문에, 401 Unauthorized HTTP 상태 코드를 반환
    //이는 클라이언트 측에서 AJAX 요청에 대한 적절한 처리를 할 수 있도록 합니다.
    if("XMLHttpRequest".equals(request.getHeader("x-requested-with"))){
      response.sendError((HttpServletResponse.SC_UNAUTHORIZED), "UnAuthorized");  //401
      //AJAX 요청이 아닌 경우,
      // 사용자가 인증되지 않았음을 알리고 로그인 페이지로 리다이렉트합니다. 이를 통해 사용자가 로그인하도록 유도합니다.
    }else{
      response.sendRedirect("/members/login");  //  나머지는 로그인을 유도하기 위해 리다이렉트 함.
    }
  }
}

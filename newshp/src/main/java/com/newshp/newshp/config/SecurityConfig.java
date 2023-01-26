package com.newshp.newshp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록됨
public class SecurityConfig {

    //해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable();

         http.authorizeRequests()
        .antMatchers("/user/**").authenticated() //로그인한 사용자만
        .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") //로그인했지만 , 로그인이나 어드민 권한이 있어야 접근가능
        .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") //어드민 권한만 접근가능
        .anyRequest().permitAll() //위 세개 주소 이외는 모든 권한 허용
        .and()
        .formLogin()
        .loginPage("/loginForm")
        .loginProcessingUrl("login") //login주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 (진행해준다)
        .defaultSuccessUrl("/");


        return http.build();
        
    }   

    //-- 리소스(URL)의 권한 설정 ↓ ↓ ↓ ↓ ↓ ↓ ↓

//     @EnableWebSecurity
// public class BrmsWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
//     @Override
//     public void configure(HttpSecurity http) throws Exception {
//         http.authorizeRequests()
//                 .antMatchers("/login**", "/web-resources/**", "/actuator/**").permitAll()
//                 .antMatchers("/admin/**").hasAnyRole("ADMIN")
//                 .antMatchers("/order/**").hasAnyRole("USER")
//                 .anyRequest().authenticated();
//     }
// }    
    //antMatchers : 특정 리소스에 대한 권한 설정
    // antMatchers("/login**", "/web-resources/**", "/actuator/**")

    //permitAll : antMachers 설정한 리소스의 접근을 인증절차 없이 허용한다는 의미
    //antMatchers("/login**", "/web-resources/**", "/actuator/**").permitAll()

    //hasAnyRole : 리소스 admin으로 시작하는 모든 URL은 인증후 ADMIN 레벨의 권한을 가진 사용자만 접근을 허용 한다는 의미
    //antMatchers("/admin/**").hasAnyRole("ADMIN")

    //anyRequest : 모든 리소스를 의미하며 접근허용 리소스 및 인증 후 특정 레벨의 권한을 가진 사용자만 접근가능한 리소스를 설정하고 그 외 나머지 리소스들은 무조건
    //인증을 완료해야 접근이 가능
    //anyRequest().authenticated()

    //-- 로그인처리 설정  ↓ ↓ ↓ ↓ ↓ ↓ ↓
    // 가장 일반 적인 로그인 방식인 로그인 FORM 페이지를 이용하여 로그인하는 방식을 사용하려고 할때 여러가지 설정을 할 수 있습니다. 
    //중요한 사실은 커스텀 필터를 적용 할때와 여러가지 설정이 중복되거나 서로 상관없는 설정이 겹치게 되어 햇갈리는 부분이 있습니다.

//     @EnableWebSecurity
// public class BrmsWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
//     @Override
//     public void configure(HttpSecurity http) throws Exception {
//         http.formLogin()
//                 .loginPage("/login-page")
//                 .loginProcessingUrl("/login-process")
//                 .defaultSuccessUrl("/main")
//                 .successHandler(new CustomAuthenticationSuccessHandler("/main"))
//                 .failureUrl("login-fail")
//                 .failureHandler(new CustomAuthenticationFailureHandler("/login-fail"))
                
//     }

    //formLogin : 로그인 페이지와 기타 로그인 처리 및 성공 실패 처리를 사용하겠다는 의미
    // 일반적인 로그인 방식 즉 로그인 폼 페이지와 로그인 처리 성공 실패 등을 사용하겠다는 의미입니다. http.formLogin()를 호출하지 않으면 완전히 로그인처리 커스텀필터를
    //만들고 설정하지 않는 이상 로그인 페이지 및 기타처리를 할 수 가 없습니다. 커스텀 필터를 만들면 사실상 필요 없는 경우도 있습니다.
    //http.formLogin(); 

    //LoginPage : 사용자가 따로 만든 로그인 페이지를 사용하려고 할때 설정합니다.
    //따로 설정하지 않으면 디폴트 URL이 “/login”이기 때문에 “/login”로 호출하면 스프링이 제공하는 기본 로그인페이지가 호출됩니다.
    //loginPage("/login-page") 

    //loginProcessingUrl : 로그인 즉 인증 처리를 하는 URL을 설정합니다. “/login-process” 가 호출되면 인증처리를 수행하는 필터가 호출됩니다.
    //로그인 FORM에서 아이디와 비번을 입력하고 확인을 클릭하면 “/login-process” 를 호출 하게 되었들 때 인증처리하는 필터가 호출되어 
    //아이디 비번을 받아와 인증처리가 수행되게 됩니다. 즉 UsernamePasswordAuthenticationFilter가 실행 되게 되는 것입니다.
    //loginProcessingUrl("/login-process") 

    //defaultSuccessUrl : 정상적으로 인증성공 했을 경우 이동하는 페이지를 설정합니다.
    //설정하지 않는경우 디폴트값은 “/” 입니다.
    //defaultSuccessUrl("/main")

    //successHandler : 정상적인증 성공 후 별도의 처리가 필요한경우 커스텀 핸들러를 생성하여 등록할 수 있습니다.
    //커스텀 핸들러를 생성하여 등록하면 인증성공 후 사용자가 추가한 로직을 수행하고 성공 페이지로 이동합니다.
    //successHandler(new CustomAuthenticationSuccessHandler("/main"))

    //failureUrl : 인증이 실패 했을 경우 이동하는 페이지를 설정합니다.
    //failureUrl("/login-fail")

    //failureHandler : 인증 실패 후 별도의 처리가 필요한경우 커스텀 핸들러를 생성하여 등록할 수 있습니다.
    //커스텀 핸들러를 생성하여 등록하면 인증실패 후 사용자가 추가한 로직을 수행하고 실패 페이지로 이동합니다.
    //successHandler(new CustomAuthenticationFailureHandler("/login-fail"))


//     @EnableWebSecurity
// public class BrmsWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
//     @Override
//     public void configure(HttpSecurity http) throws Exception {
//         http.formLogin()
//                 .loginPage("/login-page")
//                 .loginProcessingUrl("/login-process")
//                 .defaultSuccessUrl("/main")
//                 .successHandler(new CustomAuthenticationSuccessHandler("/main"))
//                 .failureUrl("login-fail")
//                 .failureHandler(new CustomAuthenticationFailureHandler("/login-fail"))
                
//     }
// } 

// 커스텀 필터 등록   ↓ ↓ ↓ ↓ ↓ ↓ ↓

// 스프링시큐리티는 각각역할에 맞는 필터들이 체인형태로 구성되서 순서에 맞게 실행되는 구조로 동작합니다. 
// 사용자는 특정 기능의 필터를 생성하여 등록할 수 있습니다. 인증을 처리하는 기본필터 UsernamePasswordAuthenticationFilter 
// 대신 별도의 인증 로직을 가진 필터를 생성하고 사용하고 싶을 때 아래와 같이 필터를 등록하고 사용합니다.

// @EnableWebSecurity
// public class BrmsWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
//     @Override
//     public void configure(HttpSecurity http) throws Exception {
//         http.addFilterBefore(new CustomAuthenticationProcessingFilter("/login-process"), 
//                 UsernamePasswordAuthenticationFilter.class);         
//     }
// } 

//addFilterBefore : 지정된 필터 앞에 커스텀 필터를 추가 (UsernamePasswordAuthenticationFilter 보다 먼저 실행된다)

//addFilterAfter : 지정된 필터 뒤에 커스텀 필터를 추가 (UsernamePasswordAuthenticationFilter 다음에 실행된다.)

//addFilterAt : 지정된 필터의 순서에 커스텀 필터가 추가된다

// 마치 지정된 필터 대신에 커스텀필터를 추가하는 것 처럼 메소드가 동작할 것 같지만 실제로는 오버라이드 되지는 않습니다. 
// 설명에는 지정된 필터의 순서와 같은 자리에 커스텀필터를 삽입한다고 되어 있고 오버라이드 되지 않는다고 설명되어 있습니다. 
// 하지만 직접테스트 결과 지정된 필터 보다 커스텀 필터가 먼저 실행되었습니다.
// 결론 적으로 어떤 메소드로 커스텀 필터를 추가하더라도 기존 필터가 오버라이드 되는 메소드는 없습니다. 
// 다만 커스텀 필터가 실행 되고 인증이 완료 되었기 때문에 UsernamePasswordAuthenticationFilter 수행되면서 인증완료된 
// 상태이면 인증 로직이 수행되지 않고 자연스럽게 통과 하기 때문에 마치 오버라이드된 것 처럼 동작하는 것으로 착각 할 수 있습니다 (직접 테스트 해봄)
    

//커스텀필터에 설정을 추가↓ ↓ ↓ ↓ ↓ ↓ ↓
//커스텀 필터에 인증 매니저 및 성공 실패 핸들러를 추가적으로 등록 및 설정을 추가 할 수 있습니다.

// @EnableWebSecurity
// public class BrmsWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

//     @Bean
//     public CustomAuthenticationProcessingFilter customAuthenticationProcessingFilter() {
//         CustomAuthenticationProcessingFilter filter = new CustomAuthenticationProcessingFilter("/login-process");
//         filter.setAuthenticationManager(new CustomAuthenticationManager());
//         filter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler("/login"));
//         filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/"));
//         return filter;
//     }

//     @Override
//     public void configure(HttpSecurity http) throws Exception {
//         http.addFilterBefore(customAuthenticationProcessingFilter(), 
//                 UsernamePasswordAuthenticationFilter.class);         
//     }
// } 

// CustomAuthenticationProcessingFilter : AbstractAuthenticationProcessingFilter 상속하여 구현하며 인증처리를 담당합니다.

// http.addFilterBefore(customAuthenticationProcessingFilter(), 
//         UsernamePasswordAuthenticationFilter.class);  

// 스프링시큐리티의 기본 인증 처리 담당 필터인 UsernamePasswordAuthenticationFilter 앞에 커스텀 필터를 추가합니다.
// UsernamePasswordAuthenticationFilter필터는 Override되지 않고 CustomAuthenticationProcessingFilter 인증 처리가 되면 자연스럽게 로직을 통과하는 구조입니다.
// 인증로직이 포함된 인증매니저 인증성공/실패 처리 핸들러 등등 기타 추가 설정 이나 기능을 대체 하는 의존성을 주입 할 수 있습니다.

//setAuthenticationManager : AuthenticationManager를 상속받아 생성하며 실제 인증로직이 포함된 커스텀 인증 매니저를 추가합니다.
//filter.setAuthenticationManager(new CustomAuthenticationManager());

//setAuthenticationFailureHandler : 인증실패 핸들러를 따로 등록 할 수 있습니다. 따로 등록하지 않으면 기본 핸들러가 동작합니다.
//filter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler("/login"));

//setAuthenticationFailureHandler
//인증실패 핸들러를 따로 등록 할 수 있습니다. 따로 등록하지 않으면 기본 핸들러가 동작합니다.
//filter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler("/login"));

}

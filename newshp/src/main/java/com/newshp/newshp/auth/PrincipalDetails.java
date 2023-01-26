package com.newshp.newshp.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.newshp.newshp.model.User;

//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 진행이 완료가 되면 시큐리티 session을 만들어줍니다.(Security ContextHolder)
//오브젝트 => Authentication 타입 객체
// Authentication 안에 User정보가 있어야 됨
// User오브젝트타입 => UserDetails 타입 객체

// Security Session => Authentication => UserDetails (PrincipalDetails)
//GrantedAuthority = 현재 사용자가 가지고 있는 권한

public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user){
        this.user = user;
    }


    //해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                
                return user.getRole();
            }
            
        });
        return collect;
    }

    @Override
    public String getPassword() {
        
        return user.getPassword();
    }

    @Override
    public String getUsername() {
       
        return user.getUsername();
    }

    //계정만료여부
    @Override
    public boolean isAccountNonExpired() {
     
        return true;
    }

    //계정잠김여부
    @Override
    public boolean isAccountNonLocked() {
        
        return false;
    }

    //계정 비밀번호 사용기간 (오래 사용하지 않았는지 여부)
    @Override
    public boolean isCredentialsNonExpired() {
       
        return true;
    }

    
    @Override
    public boolean isEnabled() {
       
        //1년동안 회원이 로그인을 안하면 , 휴면 계정으로 하기로 함
        //현재시간 - 로긴시간 =>1년 초과하면 return false;
        
        return true;
    }
    
    
    
}

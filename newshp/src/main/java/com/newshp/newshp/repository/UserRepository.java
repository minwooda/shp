package com.newshp.newshp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newshp.newshp.model.User;

public interface UserRepository extends JpaRepository<User, String> {

    //findBy규칙 -> Username문법
    //select * from user where username = 1?
    public User findByUsername(String username);
                                                                                                                                            
    //select * from user where username = 1?
    public User findByEmail(String email);
}

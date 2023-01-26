package com.newshp.newshp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newshp.newshp.model.User;

public interface UserRepository extends JpaRepository<User, String> {
     
}

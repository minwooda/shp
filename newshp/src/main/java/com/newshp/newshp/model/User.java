package com.newshp.newshp.model;



import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;


@Entity
@Data
public class User {
    
    @Id
    private String id;

    private String username; 

    private String password;

    private String email;

    private String role; //ROLE_USER,ROLE_ADMIN
    
    @CreationTimestamp
    private Timestamp createDate;
}

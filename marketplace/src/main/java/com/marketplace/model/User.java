package com.marketplace.model;

import com.marketplace.enums.Gender;
import com.marketplace.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Character")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "appUser")
public class User {
    @Id
    @GeneratedValue()
    private Long id;
    private String firstname;
    private String lastname;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String username;
    private String email;
    private String phone;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;
    private LocalDateTime suspendedAt;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profileImage_Id")
    private Image profileImage;
    private String street;
    private String city;
    private String state;
    private String zipcode;
    private String country;
}

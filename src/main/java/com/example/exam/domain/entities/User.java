package com.example.exam.domain.entities;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    private String name;
    private String password;
    private Set<UserRole> authorities;

    public User() {
        this.authorities = new HashSet<>();
    }

    @Override
    @Column(name = "name", nullable = false, unique = true, updatable = false)
    public String getUsername() {
        return this.name;
    }

    public void setUsername(String name) {
        this.name = name;
    }

    @Override
    @Column(name = "password", nullable = false)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {

    }

    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {

    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {

    }

    @Override
    @Transient
    public boolean isEnabled() {
        return true;
    }

    public void setEnabled(boolean enabled) {

    }

    @Override
    @ManyToMany(targetEntity = UserRole.class, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    public Set<UserRole> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<UserRole> authorities) {
        this.authorities = authorities;
    }
}

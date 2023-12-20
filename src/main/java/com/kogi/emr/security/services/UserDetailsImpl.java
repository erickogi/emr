package com.kogi.emr.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kogi.emr.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
  private static final long serialVersionUID = 1L;

  private Long id;

  private String email;

  private String firstName;

  private String lastName;

  private String mobileNumber;

  @JsonIgnore
  private String password;

  private Collection<? extends GrantedAuthority> authorities;
  /**
   * This constructor is used to create a UserDetailsImpl object.
   * @param id
   * @param email
   * @param password
   * @param authorities
   */
  public UserDetailsImpl(Long id, String email, String password,String firstName,String lastName,String mobileNumber,
      Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.mobileNumber = mobileNumber;
    this.authorities = authorities;
  }
  /**
   * This method is used to build a UserDetailsImpl object from a User object.
   * @param user
   * @return a UserDetailsImpl object.
   */
  public static UserDetailsImpl build(User user) {
    List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
        .collect(Collectors.toList());

    return new UserDetailsImpl(
        user.getId(),
        user.getEmail(),
        user.getPassword(),
        user.getFirstName(),
        user.getLastName(),
        user.getMobileNumber(),
        authorities);
  }
  /**
   * This method is used to get the authorities of a user.
   * @return a collection of authorities.
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
  /**
   * This method is used to check if two UserDetailsImpl objects are equal.
   * @param o object to be compared.
   * @return true if the two objects are equal, false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }
}

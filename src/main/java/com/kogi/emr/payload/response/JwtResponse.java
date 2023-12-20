package com.kogi.emr.payload.response;

import java.util.List;

public class JwtResponse {
  private String accessToken;
  private String tokenType = "Bearer";
  private Long id;
  private String username;
  private String email;

  private String firstName;

  private String lastName;

  private String mobileNumber;
  private List<String> roles;

  public JwtResponse() {
  }

  public JwtResponse(String accessToken, Long id, String username, String email,String firstName,String lastName,String mobileNumber, List<String> roles) {
    this.accessToken = accessToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.mobileNumber = mobileNumber;
    this.roles = roles;
  }

  public JwtResponse(String accessToken, String tokenType, Long id, String username, String email, List<String> roles) {
    this.accessToken = accessToken;
    this.tokenType = tokenType;
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<String> getRoles() {
    return roles;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }
}

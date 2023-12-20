package com.kogi.emr.payload.response;

public class SignUpMessageResponse {
  private String message;

  private JwtResponse jwtResponse;

  public SignUpMessageResponse(String message, JwtResponse jwtResponse) {
    this.message = message;
    this.jwtResponse = jwtResponse;
  }

  public SignUpMessageResponse(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public JwtResponse getJwtResponse() {
    return jwtResponse;
  }

  public void setJwtResponse(JwtResponse jwtResponse) {
    this.jwtResponse = jwtResponse;
  }
}

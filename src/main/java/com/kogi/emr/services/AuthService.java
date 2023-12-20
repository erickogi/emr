package com.kogi.emr.services;

import com.kogi.emr.payload.request.LoginRequest;
import com.kogi.emr.payload.request.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    /***
     * This method is used to authenticate a user.
     * @param loginRequest
     * @return 200 OK and JwtResponse if the user is authenticated successfully, 401 UNAUTHORIZED if the email or password is incorrect.
     */
    ResponseEntity<?> signIn(LoginRequest loginRequest);
    /***
     * This method is used to register a new user.
     * @param signUpRequest
     * @return 200 OK if the user is registered successfully, 400 BAD REQUEST if the email already exists.
     */
    ResponseEntity<?> signUp(SignupRequest signUpRequest);

    /***
     * This method is used to get a User.
     * @param jwtToken
     * @return 200 OK with user profile object if the user is found, 404 NOT FOUND if the user does not exist.
     */
    ResponseEntity<?> getUserProfile(String jwtToken);

}

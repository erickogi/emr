package com.kogi.emr.controllers;

import com.kogi.emr.models.User;
import com.kogi.emr.payload.request.LoginRequest;
import com.kogi.emr.payload.request.SignupRequest;
import com.kogi.emr.payload.response.JwtResponse;
import com.kogi.emr.payload.response.MessageResponse;
import com.kogi.emr.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    /**
     * Used to sign in a user.
     * @param loginRequest
     * @return 200 OK and JwtResponse if the user is authenticated successfully, 401 UNAUTHORIZED if the email or password is incorrect.
     */
    @Operation(summary = "SignIn A User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Sign In",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class))})})
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.signIn(loginRequest);
    }

    /**
     * Used to sign up a user.
     * @param signUpRequest
     * @return 200 OK if the user is registered successfully, 400 BAD REQUEST if the email already exists.
     */
    @Operation(summary = "SignUp A User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Sign Up",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class))})})
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.signUp(signUpRequest);
    }

    /**
     * Used to get a User by its jwt.
     * @param authorizationHeader
     * @return 200 OK and User Profile if the user is found, 401 UNAUTHORIZED if the token is invalid.
     */
    @Operation(summary = "Get one User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Profile",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "User Not Found or Invalid Token",
                    content = @Content)
    })
    @GetMapping("/profile")
    @PreAuthorize("hasRole('PHARMACIST') or hasRole('ADMIN')")
    public ResponseEntity<?> profile(
            @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return authService.getUserProfile( jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }
}

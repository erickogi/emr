package com.kogi.emr.services;

import com.kogi.emr.models.ERole;
import com.kogi.emr.models.Role;
import com.kogi.emr.models.User;
import com.kogi.emr.payload.request.LoginRequest;
import com.kogi.emr.payload.request.SignupRequest;
import com.kogi.emr.payload.response.JwtResponse;
import com.kogi.emr.payload.response.MessageResponse;
import com.kogi.emr.payload.response.SignUpMessageResponse;
import com.kogi.emr.repository.RoleRepository;
import com.kogi.emr.repository.UserRepository;
import com.kogi.emr.security.jwt.JwtUtils;
import com.kogi.emr.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    /**
     * This method is used to get a user from a jwt token.
     * @param jwtToken
     * @return an optional user object.
     */
    private Optional<User> getUser(String jwtToken) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
        return userRepository.findByEmail(email);
    }

    /***
     * This method is used to authenticate a user.
     * @param loginRequest
     * @return 200 OK and JwtResponse if the user is authenticated successfully, 401 UNAUTHORIZED if the email or password is incorrect.
     */
    @Override
    public ResponseEntity<?> signIn(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getMobileNumber(),
                roles));
    }

    private JwtResponse login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getMobileNumber(),
                roles);
    }

    /***
     * This method is used to register a new user.
     * @param signUpRequest
     * @return 200 OK if the user is registered successfully, 400 BAD REQUEST if the email already exists.
     */
    @Override
    public ResponseEntity<?> signUp(SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email already exists!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),signUpRequest.getFirstName(),signUpRequest.getLastName(),signUpRequest.getMobileNumber());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_PHARMACIST)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_PHARMACIST)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new SignUpMessageResponse("User registered successfully!",login(new LoginRequest(signUpRequest.getEmail(),signUpRequest.getPassword()))));
    }

    /**
     * This method is used to get a User.
     * @param jwtToken
     * @return 200 OK with user profile object if the user is found, 404 NOT FOUND if the user does not exist.
     */
    @Override
    public ResponseEntity<?> getUserProfile(String jwtToken) {
        Optional<User> user = getUser(jwtToken);

        if(user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }else {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }
}
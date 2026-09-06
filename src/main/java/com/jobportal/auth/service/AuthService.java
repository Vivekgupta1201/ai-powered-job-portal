package com.jobportal.auth.service;


import com.jobportal.auth.dto.AuthResponse;
import com.jobportal.auth.dto.LoginRequest;
import com.jobportal.auth.dto.LoginResponse;
import com.jobportal.auth.dto.RegisterRequest;
import com.jobportal.auth.entity.Role;
import com.jobportal.auth.entity.User;
import com.jobportal.auth.entity.UserStatus;
import com.jobportal.common.exception.ConflictException;
import com.jobportal.common.exception.NotFoundException;
import com.jobportal.common.exception.UnauthorizedException;
import com.jobportal.auth.repository.RoleRepository;
import com.jobportal.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
this.userRepository = userRepository;
this.roleRepository = roleRepository;
this.passwordEncoder = passwordEncoder;
this.jwtService = jwtService;
}

    public AuthResponse registerJobSeeker(RegisterRequest request) {
        return registerUser(request, "ROLE_JOB_SEEKER");
    }

    public AuthResponse registerRecruiter(RegisterRequest request) {
        return registerUser(request, "ROLE_RECRUITER");
    }

    private AuthResponse registerUser(RegisterRequest request, String roleName) {
        String email = normalizeEmail(request.getEmail());

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email is already registered");
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException("Role not found: " + roleName));

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.getRoles().add(role);

        User savedUser = userRepository.save(user);

        return new AuthResponse(
                "User registered successfully",
                savedUser.getEmail(),
                role.getName()
        );
    }
    
    public LoginResponse login(LoginRequest request) {
        String email = normalizeEmail(request.getEmail());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

        if (!passwordMatches) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String role = user.getRoles()
                .stream()
                .findFirst()
                .map(Role::getName)
                .orElseThrow(() -> new UnauthorizedException("User role is missing"));

        String token = jwtService.generateToken(user);

        return new LoginResponse(token, user.getEmail(), role);
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
   

}


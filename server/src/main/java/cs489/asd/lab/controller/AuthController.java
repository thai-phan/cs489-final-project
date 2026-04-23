package cs489.asd.lab.controller;

import cs489.asd.lab.config.JwtTokenService;
import cs489.asd.lab.dto.LoginRequest;
import cs489.asd.lab.dto.LoginResponse;
import cs489.asd.lab.dto.RegisterRequest;
import cs489.asd.lab.model.Manager;
import cs489.asd.lab.model.Patient;
import cs489.asd.lab.model.Role;
import cs489.asd.lab.model.User;
import cs489.asd.lab.repository.ManagerRepository;
import cs489.asd.lab.repository.PatientRepository;
import cs489.asd.lab.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/adsweb/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    ManagerRepository managerRepository;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtTokenService jwtTokenService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest == null || isBlank(loginRequest.email()) || isBlank(loginRequest.password())) {
            throw new BadCredentialsException("email and password are required");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        return toLoginResponse(authentication);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public LoginResponse register(@RequestBody RegisterRequest registerRequest) {
        if (registerRequest == null || isBlank(registerRequest.email()) || isBlank(registerRequest.password())) {
            throw new BadCredentialsException("email and password are required");
        }

        if (userRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new BadCredentialsException("email is already in use");
        }
        Role patientRole;
        if (registerRequest.isAdmin()) {
            patientRole = userRepository.findRoleByName("MANAGER")
                    .orElseThrow(() -> new IllegalStateException("Required role MANAGER not found"));

        } else {
            patientRole = userRepository.findRoleByName("PATIENT")
                    .orElseThrow(() -> new IllegalStateException("Required role PATIENT not found"));

        }

        User user = new User();
        user.setFirstName(registerRequest.firstName().trim());
        user.setLastName(registerRequest.lastName().trim());
        user.setEmail(registerRequest.email().trim());
        user.setPhoneNumber(registerRequest.phoneNumber());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setEnabled(true);
        user.setRole(patientRole);
        userRepository.save(user);

        if (registerRequest.isAdmin()) {
            Manager manager = new Manager();
            manager.setUser(user);
            manager.setOfficeLocation("Head Office");
            managerRepository.save(manager);
        } else {
            Patient patient = new Patient();
            patient.setUser(user);
            patient.setDateOfBirth(LocalDate.now());
            patient.setMailingAddress(registerRequest.email().trim());
            patientRepository.save(patient);
        }


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.email().trim(), registerRequest.password())
        );

        return toLoginResponse(authentication);
    }

    private LoginResponse toLoginResponse(Authentication authentication) {
        String token = jwtTokenService.generateToken(authentication);
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new LoginResponse(
                token,
                "Bearer",
                jwtTokenService.getExpirationSeconds(),
                authentication.getName(),
                roles
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

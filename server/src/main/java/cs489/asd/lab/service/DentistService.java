package cs489.asd.lab.service;

import cs489.asd.lab.dto.DentistCreateRequest;
import cs489.asd.lab.dto.DentistView;
import cs489.asd.lab.model.Dentist;
import cs489.asd.lab.model.Role;
import cs489.asd.lab.model.User;
import cs489.asd.lab.repository.DentistRepository;
import cs489.asd.lab.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class DentistService {

    private final DentistRepository dentistRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DentistService(
            DentistRepository dentistRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.dentistRepository = dentistRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public DentistView createDentist(DentistCreateRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dentist payload is required");
        }

        String firstName = requireText(request.firstName(), "firstName");
        String lastName = requireText(request.lastName(), "lastName");
        String email = requireText(request.email(), "email");
        String phoneNumber = requireText(request.phoneNumber(), "phoneNumber");
        String password = requireText(request.password(), "password");
        String dentistIdNumber = requireText(request.dentistIdNumber(), "dentistIdNumber");
        String specialization = requireText(request.specialization(), "specialization");

//        if (userRepository.findByEmail(email).isPresent()) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "email is already in use");
//        }
//        if (dentistRepository.findByDentistIdNumber(dentistIdNumber).isPresent()) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "dentistIdNumber is already in use");
//        }

        Role dentistRole = userRepository.findRoleByName("DENTIST")
                .orElseThrow(() -> new IllegalStateException("Required role DENTIST not found"));

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        user.setRole(dentistRole);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        Dentist dentist = new Dentist();
        dentist.setUser(savedUser);
        dentist.setDentistIdNumber(dentistIdNumber);
        dentist.setSpecialization(specialization);

        Dentist savedDentist = dentistRepository.save(dentist);
        return toView(savedDentist);
    }

    private String requireText(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " is required");
        }
        return value.trim();
    }

    private DentistView toView(Dentist dentist) {
        return new DentistView(
                dentist.getDentistIdNumber(),
                dentist.getUser().getFirstName(),
                dentist.getUser().getLastName(),
                dentist.getUser().getPhoneNumber(),
                dentist.getUser().getEmail(),
                dentist.getSpecialization()
        );
    }
}



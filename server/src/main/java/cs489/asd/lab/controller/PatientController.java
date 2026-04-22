package cs489.asd.lab.controller;

import cs489.asd.lab.dto.AddressResponse;
import cs489.asd.lab.dto.PatientRequest;
import cs489.asd.lab.dto.PatientResponse;
import cs489.asd.lab.model.Patient;
import cs489.asd.lab.repository.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/adsweb/api/v1")
@PreAuthorize("hasAnyRole('ADMIN','STAFF','DENTIST','HYGIENIST')")
public class PatientController {

    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping("/patients")
    public List<PatientResponse> listPatients() {
        return patientRepository.findAllByOrderByLastNameAscFirstNameAscPatientIdAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/patients/{patientId}")
    public PatientResponse getPatient(@PathVariable long patientId) {
        return toResponse(findPatientOrThrow(patientId));
    }

    @PostMapping("/patients")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @ResponseStatus(HttpStatus.CREATED)
    public PatientResponse createPatient(@RequestBody PatientRequest request) {
        validate(request);
        Patient saved = patientRepository.save(toEntity(request));
        return toResponse(saved);
    }

    @PutMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public PatientResponse updatePatient(@PathVariable long patientId, @RequestBody PatientRequest request) {
        validate(request);
        Patient patient = findPatientOrThrow(patientId);
        patient.setFirstName(request.firstName());
        patient.setLastName(request.lastName());
        patient.setContactPhone(request.contactPhone());
        patient.setEmail(request.email());
        patient.setMailingAddress(request.mailingAddress());
        patient.setDateOfBirth(request.dateOfBirth());
        return toResponse(patientRepository.save(patient));
    }

    @DeleteMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable long patientId) {
        Patient patient = findPatientOrThrow(patientId);
        patientRepository.delete(patient);
    }

    @GetMapping("/patient/search/{searchString}")
    public List<PatientResponse> searchPatients(@PathVariable String searchString) {
        String term = searchString == null ? "" : searchString.trim();
        if (term.isEmpty()) {
            return listPatients();
        }

        return patientRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrContactPhoneContainingIgnoreCaseOrMailingAddressContainingIgnoreCaseOrderByLastNameAscFirstNameAscPatientIdAsc(
                        term, term, term, term, term)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/addresses")
    public List<AddressResponse> listAddresses() {
        return patientRepository.findAllByOrderByLastNameAscFirstNameAscPatientIdAsc()
                .stream()
                .map(this::toAddressResponse)
                .sorted(Comparator.comparing(AddressResponse::city, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(AddressResponse::lastName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(AddressResponse::firstName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparingLong(AddressResponse::patientId))
                .toList();
    }

    private Patient findPatientOrThrow(long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
    }

    private void validate(PatientRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Patient payload is required");
        }
        requireText(request.firstName(), "firstName");
        requireText(request.lastName(), "lastName");
        requireText(request.contactPhone(), "contactPhone");
        requireText(request.email(), "email");
        requireText(request.mailingAddress(), "mailingAddress");
        if (request.dateOfBirth() == null) {
            throw new IllegalArgumentException("dateOfBirth is required");
        }
    }

    private void requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
    }

    private Patient toEntity(PatientRequest request) {
        return new Patient(
                request.firstName(),
                request.lastName(),
                request.contactPhone(),
                request.email(),
                request.mailingAddress(),
                request.dateOfBirth()
        );
    }

    private PatientResponse toResponse(Patient patient) {
        return new PatientResponse(
                patient.getPatientId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getContactPhone(),
                patient.getEmail(),
                patient.getMailingAddress(),
                patient.getDateOfBirth()
        );
    }

    private AddressResponse toAddressResponse(Patient patient) {
        return new AddressResponse(
                patient.getPatientId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getEmail(),
                patient.getContactPhone(),
                patient.getMailingAddress(),
                extractCity(patient.getMailingAddress()),
                patient.getDateOfBirth()
        );
    }

    private String extractCity(String mailingAddress) {
        if (mailingAddress == null || mailingAddress.isBlank()) {
            return "";
        }

        int commaIndex = mailingAddress.lastIndexOf(',');
        if (commaIndex >= 0 && commaIndex + 1 < mailingAddress.length()) {
            return mailingAddress.substring(commaIndex + 1).trim();
        }

        return mailingAddress.trim();
    }

}

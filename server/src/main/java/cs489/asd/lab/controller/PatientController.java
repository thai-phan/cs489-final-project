package cs489.asd.lab.controller;

import cs489.asd.lab.controller.common.PatientNotFoundException;
import cs489.asd.lab.dto.PatientResponse;
import cs489.asd.lab.model.Patient;
import cs489.asd.lab.repository.PatientRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/adsweb/api/v1")
@PreAuthorize("hasAnyRole('MANAGER','DENTIST')")
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

    private Patient findPatientOrThrow(long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
    }

    private PatientResponse toResponse(Patient patient) {
        return new PatientResponse(
                patient.getUserId(),
                patient.getUser().getFirstName(),
                patient.getUser().getLastName(),
                patient.getUser().getPhoneNumber(),
                patient.getUser().getEmail(),
                patient.getMailingAddress(),
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

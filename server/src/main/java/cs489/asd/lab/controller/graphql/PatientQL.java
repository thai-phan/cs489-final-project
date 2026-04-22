package cs489.asd.lab.controller.graphql;

import cs489.asd.lab.dto.AddressView;
import cs489.asd.lab.dto.PatientView;
import cs489.asd.lab.model.Address;
import cs489.asd.lab.model.Patient;
import cs489.asd.lab.repository.PatientRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.Comparator;
import java.util.List;

@Controller
public class PatientQL {

    private final PatientRepository patientRepository;

    public PatientQL(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','DENTIST','HYGIENIST')")
    public List<PatientView> patients() {
        return patientRepository.findAllByOrderByLastNameAscFirstNameAscPatientIdAsc()
                .stream()
                .map(this::toPatientView)
                .toList();
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','DENTIST','HYGIENIST')")
    public PatientView patient(@Argument long patientId) {
        return patientRepository.findById(patientId)
                .map(this::toPatientView)
                .orElse(null);
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','DENTIST','HYGIENIST')")
    public List<PatientView> searchPatients(@Argument String searchString) {
        String term = searchString == null ? "" : searchString.trim();
        if (term.isEmpty()) {
            return patients();
        }

        return patientRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrContactPhoneContainingIgnoreCaseOrMailingAddressContainingIgnoreCaseOrderByLastNameAscFirstNameAscPatientIdAsc(
                        term, term, term, term, term)
                .stream()
                .map(this::toPatientView)
                .toList();
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','DENTIST','HYGIENIST')")
    public List<AddressView> addresses() {
        return patientRepository.findAllByOrderByLastNameAscFirstNameAscPatientIdAsc()
                .stream()
                .map(this::toAddressView)
                .sorted(Comparator.comparing(AddressView::city, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(AddressView::lastName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(AddressView::firstName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparingLong(AddressView::patientId))
                .toList();
    }

    private PatientView toPatientView(Patient patient) {
        return new PatientView(
                patient.getPatientId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getContactPhone(),
                patient.getEmail(),
                patient.getMailingAddress(),
                patient.getDateOfBirth() == null ? null : patient.getDateOfBirth().toString()
        );
    }

    private AddressView toAddressView(Patient patient) {
        return new AddressView(
                patient.getPatientId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getEmail(),
                patient.getContactPhone(),
                patient.getMailingAddress(),
                Address.extractCity(patient.getMailingAddress()),
                patient.getDateOfBirth() == null ? null : patient.getDateOfBirth().toString()
        );
    }
}

package cs489.asd.lab.controller.graphql;

import cs489.asd.lab.dto.DentistView;
import cs489.asd.lab.model.Dentist;
import cs489.asd.lab.repository.DentistRepository;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class DentistQL {

    private final DentistRepository dentistRepository;

    public DentistQL(DentistRepository dentistRepository) {
        this.dentistRepository = dentistRepository;
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN','DENTIST','PATIENT')")
    public List<DentistView> dentists() {
        return dentistRepository.findAllByOrderByLastNameAscFirstNameAscDentistIdAsc()
                .stream()
                .map(this::toDentistView)
                .toList();
    }

    private DentistView toDentistView(Dentist dentist) {
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

package cs489.asd.lab.controller;

import cs489.asd.lab.dto.DentistView;
import cs489.asd.lab.model.Dentist;
import cs489.asd.lab.repository.DentistRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/adsweb/api/v1")
@PreAuthorize("hasAnyRole('MANAGER','DENTIST','PATIENT')")
public class DentistController {

    private final DentistRepository dentistRepository;

    public DentistController(DentistRepository dentistRepository) {
        this.dentistRepository = dentistRepository;
    }

    @GetMapping("/dentists")
    public List<DentistView> listDentists() {
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

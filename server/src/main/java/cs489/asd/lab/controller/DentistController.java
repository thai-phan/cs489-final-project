package cs489.asd.lab.controller;

import cs489.asd.lab.dto.DentistView;
import cs489.asd.lab.dto.DentistCreateRequest;
import cs489.asd.lab.model.Dentist;
import cs489.asd.lab.repository.DentistRepository;
import cs489.asd.lab.service.DentistService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/adsweb/api/v1")
public class DentistController {

    private final DentistRepository dentistRepository;
    private final DentistService dentistService;

    public DentistController(DentistRepository dentistRepository, DentistService dentistService) {
        this.dentistRepository = dentistRepository;
        this.dentistService = dentistService;
    }

    @GetMapping("/dentists")
    @PreAuthorize("hasAnyRole('MANAGER','DENTIST','PATIENT')")
    public List<DentistView> listDentists() {
        return dentistRepository.findAllByOrderByLastNameAscFirstNameAscDentistIdAsc()
                .stream()
                .map(this::toDentistView)
                .toList();
    }

    @GetMapping("/dentist-names")
    @PreAuthorize("hasAnyRole('MANAGER','DENTIST','PATIENT')")
    public List<String> listDentistNames() {
        return dentistRepository.findAllByOrderByLastNameAscFirstNameAscDentistIdAsc()
                .stream()
                .map(dentist -> "Dr. " + dentist.getUser().getFirstName() + " " + dentist.getUser().getLastName())
                .toList();
    }

    @PostMapping("/dentists")
    @PreAuthorize("hasRole('MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    public DentistView addDentist(@RequestBody DentistCreateRequest request) {
        return dentistService.createDentist(request);
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

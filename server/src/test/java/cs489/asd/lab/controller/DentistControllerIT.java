package cs489.asd.lab.controller;

import cs489.asd.lab.model.Dentist;
import cs489.asd.lab.model.Role;
import cs489.asd.lab.model.User;
import cs489.asd.lab.repository.DentistRepository;
import cs489.asd.lab.service.DentistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class DentistControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @SuppressWarnings("unused")
    @MockitoBean
    private DentistRepository dentistRepository;

    @SuppressWarnings("unused")
    @MockitoBean
    private DentistService dentistService;

    @Test
    @WithMockUser(roles = "PATIENT")
    void listDentists_shouldReturnDentistsForAuthenticatedPatient() throws Exception {
        given(dentistRepository.findAllByOrderByLastNameAscFirstNameAscDentistIdAsc())
                .willReturn(List.of(buildDentist("D-001", "Ava", "Nguyen", "555-0101", "ava.nguyen@example.com", "Orthodontics")));

        mockMvc().perform(get("/adsweb/api/v1/dentists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dentistId").value("D-001"))
                .andExpect(jsonPath("$[0].firstName").value("Ava"))
                .andExpect(jsonPath("$[0].lastName").value("Nguyen"))
                .andExpect(jsonPath("$[0].phoneNumber").value("555-0101"))
                .andExpect(jsonPath("$[0].email").value("ava.nguyen@example.com"))
                .andExpect(jsonPath("$[0].specialization").value("Orthodontics"));
    }

    @Test
    void listDentists_shouldRejectUnauthenticatedRequests() throws Exception {
        mockMvc().perform(get("/adsweb/api/v1/dentists"))
                .andExpect(status().isUnauthorized());
    }

    private MockMvc mockMvc() {
        return MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    private Dentist buildDentist(
            String dentistId,
            String firstName,
            String lastName,
            String phoneNumber,
            String email,
            String specialization
    ) {
        Role role = new Role();
        role.setRoleName("DENTIST");

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setRole(role);

        Dentist dentist = new Dentist();
        dentist.setDentistIdNumber(dentistId);
        dentist.setUser(user);
        dentist.setSpecialization(specialization);
        return dentist;
    }
}


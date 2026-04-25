package cs489.asd.lab.controller;

import cs489.asd.lab.model.*;
import cs489.asd.lab.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/")
@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InitController {

    private final RoleRepository roleRepository;
    private final AppointmentStatusRepository appointmentStatusRepository;
    private final SurgeryRepository surgeryRepository;
    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final DentistRepository dentistRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;

    public InitController(RoleRepository roleRepository,
                          AppointmentStatusRepository appointmentStatusRepository,
                          SurgeryRepository surgeryRepository,
                          UserRepository userRepository,
                          ManagerRepository managerRepository,
                          DentistRepository dentistRepository,
                          PatientRepository patientRepository,
                          AppointmentRepository appointmentRepository,
                          BillRepository billRepository) {
        this.roleRepository = roleRepository;
        this.appointmentStatusRepository = appointmentStatusRepository;
        this.surgeryRepository = surgeryRepository;
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
        this.dentistRepository = dentistRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.billRepository = billRepository;
    }

    @GetMapping("/init")
    @Transactional
    public ResponseEntity<String> seedDatabase() {
        // Roles
        Role managerRole = ensureRole("ROLE_MANAGER");
        Role dentistRole = ensureRole("ROLE_DENTIST");
        Role patientRole = ensureRole("ROLE_PATIENT");

        // Appointment statuses
        AppointmentStatus pending = ensureStatus("PENDING");
        AppointmentStatus scheduled = ensureStatus("SCHEDULED");
        AppointmentStatus completed = ensureStatus("COMPLETED");
        AppointmentStatus cancelled = ensureStatus("CANCELLED");

        // Surgeries
        Surgery surgery1 = ensureSurgery(
                "Main Street Dental Surgery",
                "100 Main Street, Boston",
                "555-0301"
        );
        Surgery surgery2 = ensureSurgery(
                "North Clinic Surgery",
                "25 North Avenue, Cambridge",
                "555-0302"
        );
        Surgery surgery3 = ensureSurgery(
                "Harbor Dental Care",
                "14 Harbor Road, Quincy",
                "555-0303"
        );
        Surgery surgery4 = ensureSurgery(
                "Lakeside Family Surgery",
                "78 Lake Street, Somerville",
                "555-0304"
        );
        Surgery surgery5 = ensureSurgery(
                "Downtown Smile Studio",
                "220 Center Plaza, Salem",
                "555-0305"
        );

        return ResponseEntity.ok("Seed data inserted successfully.");
    }

    private Role ensureRole(String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        return roleRepository.save(role);
    }

    private AppointmentStatus ensureStatus(String statusName) {
        AppointmentStatus status = new AppointmentStatus();
        status.setStatusName(statusName);
        return appointmentStatusRepository.save(status);
    }

    private Surgery ensureSurgery(String name, String locationAddress, String telephoneNumber) {
        Surgery surgery = new Surgery();
        surgery.setName(name);
        surgery.setLocationAddress(locationAddress);
        surgery.setTelephoneNumber(telephoneNumber);
        return surgeryRepository.save(surgery);
    }
}
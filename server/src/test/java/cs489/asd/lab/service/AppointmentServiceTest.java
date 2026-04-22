package cs489.asd.lab.service;

import cs489.asd.lab.controller.common.BusinessRuleViolationException;
import cs489.asd.lab.dto.AppointmentDetailsView;
import cs489.asd.lab.dto.AppointmentRequest;
import cs489.asd.lab.dto.AppointmentResponse;
import cs489.asd.lab.model.Appointment;
import cs489.asd.lab.model.AppointmentStatus;
import cs489.asd.lab.model.Dentist;
import cs489.asd.lab.model.Patient;
import cs489.asd.lab.model.Surgery;
import cs489.asd.lab.repository.AppointmentRepository;
import cs489.asd.lab.repository.BillRepository;
import cs489.asd.lab.repository.DentistRepository;
import cs489.asd.lab.repository.PatientRepository;
import cs489.asd.lab.repository.SurgeryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private BillRepository billRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DentistRepository dentistRepository;
    @Mock
    private SurgeryRepository surgeryRepository;

    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentService(
                appointmentRepository,
                billRepository,
                patientRepository,
                dentistRepository,
                surgeryRepository
        );
    }

    @Test
    void requestAppointment_rejectsWhenPatientHasUnpaidBill() {
        AppointmentRequest request = new AppointmentRequest(1L, 2L, 3L, "2026-04-20T09:00:00");
        stubLookups();
        when(billRepository.existsUnpaidByPatientId(1L)).thenReturn(true);

        assertThrows(BusinessRuleViolationException.class, () -> appointmentService.requestAppointment(request));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void requestAppointment_rejectsWhenDentistAlreadyHasFiveAppointmentsInWeek() {
        AppointmentRequest request = new AppointmentRequest(1L, 2L, 3L, "2026-04-20T09:00:00");
        stubLookups();
        when(billRepository.existsUnpaidByPatientId(1L)).thenReturn(false);
        when(appointmentRepository.countDentistAppointmentsBetween(anyLong(), any(), any())).thenReturn(5L);

        assertThrows(BusinessRuleViolationException.class, () -> appointmentService.requestAppointment(request));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void requestAppointment_savesWhenRulesAreSatisfied() {
        AppointmentRequest request = new AppointmentRequest(1L, 2L, 3L, "2026-04-20T09:00:00");
        stubLookups();
        when(billRepository.existsUnpaidByPatientId(1L)).thenReturn(false);
        when(appointmentRepository.countDentistAppointmentsBetween(anyLong(), any(), any())).thenReturn(4L);
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> {
            Appointment appointment = invocation.getArgument(0);
            appointment.setAppointmentId(42L);
            return appointment;
        });

        AppointmentResponse response = appointmentService.requestAppointment(request);

        assertEquals(42L, response.appointmentId());
        assertEquals("SCHEDULED", response.status());
        assertEquals("2026-04-20T09:00", response.appointmentDateTime());
        assertEquals(1L, response.patientId());
        assertEquals(2L, response.dentistId());
        assertEquals(3L, response.surgeryId());
    }

    @Test
    void getAppointmentsByDentist_includesPatientDetails() {
        Appointment appointment = createAppointment(77L, 2L, 1L, 3L, LocalDateTime.of(2026, 4, 20, 9, 0));
        when(appointmentRepository.findByDentistIdOrderByDateTimeAsc(2L)).thenReturn(List.of(appointment));

        List<AppointmentDetailsView> results = appointmentService.getAppointmentsByDentist(2L);

        assertEquals(1, results.size());
        assertEquals(77L, results.get(0).appointmentId());
        assertEquals("2026-04-20T09:00", results.get(0).appointmentDateTime());
        assertEquals(1L, results.get(0).patient().patientId());
        assertEquals("John", results.get(0).patient().firstName());
        assertEquals("100 Main Street, Boston", results.get(0).surgeryLocation());
    }

    @Test
    void getAppointmentsBySurgeryLocation_requiresNonBlankLocation() {
        assertThrows(IllegalArgumentException.class, () -> appointmentService.getAppointmentsBySurgeryLocation("   "));
    }

    @Test
    void getAppointmentsByPatientAndDate_rejectsInvalidDateFormat() {
        assertThrows(IllegalArgumentException.class, () -> appointmentService.getAppointmentsByPatientAndDate(1L, "04/20/2026"));
    }

    @Test
    void getAppointmentsByPatientAndDate_returnsFilteredResults() {
        Appointment appointment = createAppointment(88L, 2L, 1L, 3L, LocalDateTime.of(2026, 4, 20, 14, 30));
        when(appointmentRepository.findByPatientIdAndDateOrderByDateTimeAsc(1L, LocalDate.of(2026, 4, 20)))
                .thenReturn(List.of(appointment));

        List<AppointmentDetailsView> results = appointmentService.getAppointmentsByPatientAndDate(1L, "2026-04-20");

        assertEquals(1, results.size());
        assertEquals(88L, results.get(0).appointmentId());
        assertEquals("Smith", results.get(0).patient().lastName());
    }

    private void stubLookups() {
        Patient patient = new Patient("John", "Smith", "555-0101", "john.smith@example.com", "12 Oak Lane, Boston", LocalDate.of(1988, 2, 14));
        patient.setPatientId(1L);

        Dentist dentist = new Dentist();
        dentist.setDentistId(2L);

        Surgery surgery = new Surgery();
        surgery.setSurgeryId(3L);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(dentistRepository.findById(2L)).thenReturn(Optional.of(dentist));
        when(surgeryRepository.findById(3L)).thenReturn(Optional.of(surgery));
    }

    private Appointment createAppointment(long appointmentId, long dentistId, long patientId, long surgeryId, LocalDateTime dateTime) {
        Patient patient = new Patient("John", "Smith", "555-0101", "john.smith@example.com", "12 Oak Lane, Boston", LocalDate.of(1988, 2, 14));
        patient.setPatientId(patientId);

        Dentist dentist = new Dentist();
        dentist.setDentistId(dentistId);

        Surgery surgery = new Surgery();
        surgery.setSurgeryId(surgeryId);
        surgery.setLocationAddress("100 Main Street, Boston");

        Appointment appointment = new Appointment();
        appointment.setAppointmentId(appointmentId);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setDentist(dentist);
        appointment.setPatient(patient);
        appointment.setSurgery(surgery);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        return appointment;
    }
}


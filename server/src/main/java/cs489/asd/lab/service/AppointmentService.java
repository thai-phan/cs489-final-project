package cs489.asd.lab.service;

import cs489.asd.lab.controller.BusinessRuleViolationException;
import cs489.asd.lab.dto.AppointmentDetailsView;
import cs489.asd.lab.dto.AppointmentRequest;
import cs489.asd.lab.dto.AppointmentResponse;
import cs489.asd.lab.dto.PatientView;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class AppointmentService {

    private static final int MAX_DENTIST_APPOINTMENTS_PER_WEEK = 5;

    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;
    private final SurgeryRepository surgeryRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            BillRepository billRepository,
            PatientRepository patientRepository,
            DentistRepository dentistRepository,
            SurgeryRepository surgeryRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.billRepository = billRepository;
        this.patientRepository = patientRepository;
        this.dentistRepository = dentistRepository;
        this.surgeryRepository = surgeryRepository;
    }

    @Transactional
    public AppointmentResponse requestAppointment(AppointmentRequest request) {
        validateRequest(request);
        LocalDateTime appointmentDateTime = parseDateTime(request.appointmentDateTime());

        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + request.patientId()));

        Dentist dentist = dentistRepository.findById(request.dentistId())
                .orElseThrow(() -> new IllegalArgumentException("Dentist not found: " + request.dentistId()));

        Surgery surgery = surgeryRepository.findById(request.surgeryId())
                .orElseThrow(() -> new IllegalArgumentException("Surgery not found: " + request.surgeryId()));

        if (billRepository.existsUnpaidByPatientId(patient.getPatientId())) {
            throw new BusinessRuleViolationException(
                    "Patient has an outstanding unpaid bill and cannot request a new appointment"
            );
        }

        LocalDate weekDate = appointmentDateTime.toLocalDate();
        LocalDateTime weekStart = weekDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime weekEnd = weekStart.plusWeeks(1);

        long dentistAppointments = appointmentRepository.countDentistAppointmentsBetween(
                dentist.getDentistId(),
                weekStart,
                weekEnd
        );

        if (dentistAppointments >= MAX_DENTIST_APPOINTMENTS_PER_WEEK) {
            throw new BusinessRuleViolationException(
                    "Dentist already has 5 appointments in the selected week"
            );
        }

        Appointment appointment = new Appointment();
        appointment.setAppointmentDateTime(appointmentDateTime);
        appointment.setPatient(patient);
        appointment.setDentist(dentist);
        appointment.setSurgery(surgery);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment saved = appointmentRepository.save(appointment);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AppointmentDetailsView> getAppointmentsByDentist(long dentistId) {
        requirePositive(dentistId, "dentistId");
        return appointmentRepository.findByDentistIdOrderByDateTimeAsc(dentistId)
                .stream()
                .map(this::toDetailsView)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AppointmentDetailsView> getAppointmentsBySurgeryLocation(String locationAddress) {
        String normalizedLocation = requireText(locationAddress, "locationAddress");
        return appointmentRepository.findBySurgeryLocationOrderByDateTimeAsc(normalizedLocation)
                .stream()
                .map(this::toDetailsView)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AppointmentDetailsView> getAppointmentsByPatientAndDate(long patientId, String appointmentDate) {
        requirePositive(patientId, "patientId");
        LocalDate date = parseDate(appointmentDate);
        return appointmentRepository.findByPatientIdAndDateOrderByDateTimeAsc(patientId, date)
                .stream()
                .map(this::toDetailsView)
                .toList();
    }

    private void validateRequest(AppointmentRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Appointment payload is required");
        }
        requirePositive(request.patientId(), "patientId");
        requirePositive(request.dentistId(), "dentistId");
        requirePositive(request.surgeryId(), "surgeryId");

        if (request.appointmentDateTime() == null || request.appointmentDateTime().trim().isEmpty()) {
            throw new IllegalArgumentException("appointmentDateTime is required");
        }
    }

    private void requirePositive(long value, String field) {
        if (value <= 0) {
            throw new IllegalArgumentException(field + " must be a positive number");
        }
    }

    private String requireText(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value.trim();
    }

    private LocalDateTime parseDateTime(String value) {
        try {
            return LocalDateTime.parse(value.trim());
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("appointmentDateTime must be ISO-8601 local datetime, e.g. 2026-04-20T09:00:00");
        }
    }

    private LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(requireText(value, "appointmentDate"));
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("appointmentDate must be ISO-8601 date, e.g. 2026-04-20");
        }
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getAppointmentId(),
                appointment.getAppointmentDateTime() == null ? null : appointment.getAppointmentDateTime().toString(),
                appointment.getDentist().getDentistId(),
                appointment.getPatient().getPatientId(),
                appointment.getSurgery().getSurgeryId(),
                appointment.getStatus().name()
        );
    }

    private AppointmentDetailsView toDetailsView(Appointment appointment) {
        Patient patient = appointment.getPatient();

        PatientView patientView = new PatientView(
                patient.getPatientId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getContactPhone(),
                patient.getEmail(),
                patient.getMailingAddress(),
                patient.getDateOfBirth() == null ? null : patient.getDateOfBirth().toString()
        );

        return new AppointmentDetailsView(
                appointment.getAppointmentId(),
                appointment.getAppointmentDateTime() == null ? null : appointment.getAppointmentDateTime().toString(),
                appointment.getDentist().getDentistId(),
                appointment.getSurgery().getSurgeryId(),
                appointment.getStatus().name(),
                appointment.getSurgery().getLocationAddress(),
                patientView
        );
    }
}


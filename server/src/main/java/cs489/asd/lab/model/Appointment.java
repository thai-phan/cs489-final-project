package cs489.asd.lab.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long appointmentId;

    @Column(name = "appointment_date_time", nullable = false)
    private LocalDateTime appointmentDateTime;

    @ManyToOne
    @JoinColumn(name = "dentist_user_id")
    private Dentist dentist;

    @ManyToOne
    @JoinColumn(name = "patient_user_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "surgery_id")
    private Surgery surgery;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private AppointmentStatus status;
}
package cs489.asd.lab.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "surgeries")
@Getter
@Setter
@NoArgsConstructor
public class Surgery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "surgery_id")
    private Long surgeryId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location_address", nullable = false)
    private String locationAddress;

    @Column(name = "telephone_number", nullable = false)
    private String telephoneNumber;

    @OneToMany(mappedBy = "surgery")
    private List<Appointment> appointments;
}

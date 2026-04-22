package cs489.asd.lab.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "patients")
@Setter
@Getter
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "contact_phone", nullable = false)
    private String contactPhone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Address primaryAddress;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bill> bills;

    public Patient(String firstName, String lastName, String contactPhone, String email, String mailingAddress, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactPhone = contactPhone;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        setMailingAddress(mailingAddress);
    }

    @Transient
    public String getMailingAddress() {
        return primaryAddress == null ? null : primaryAddress.getMailingAddress();
    }

    public void setMailingAddress(String mailingAddress) {
        if (mailingAddress == null || mailingAddress.isBlank()) {
            setPrimaryAddress(null);
            return;
        }

        Address address = primaryAddress == null ? new Address() : primaryAddress;
        address.setMailingAddress(mailingAddress.trim());
        address.setCity(Address.extractCity(mailingAddress));
        setPrimaryAddress(address);
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
        if (primaryAddress != null && primaryAddress.getPatient() != this) {
            primaryAddress.setPatient(this);
        }
    }

    // Helper method for the business rule: check for unpaid bills
    public boolean hasOutstandingBills() {
        return bills != null && bills.stream().anyMatch(bill -> !bill.isPaid());
    }
}
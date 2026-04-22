package cs489.asd.lab.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "mailing_address", nullable = false)
    private String mailingAddress;

    @Column(name = "city", nullable = false)
    private String city;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patient;

    public static String extractCity(String mailingAddress) {
        if (mailingAddress == null || mailingAddress.isBlank()) {
            return "";
        }

        int commaIndex = mailingAddress.lastIndexOf(',');
        if (commaIndex >= 0 && commaIndex + 1 < mailingAddress.length()) {
            return mailingAddress.substring(commaIndex + 1).trim();
        }

        return mailingAddress.trim();
    }
}


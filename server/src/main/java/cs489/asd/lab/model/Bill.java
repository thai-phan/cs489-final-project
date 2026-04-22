package cs489.asd.lab.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "bills")
@Getter
@Setter
@NoArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Long billId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "paid", nullable = false)
    private boolean paid;

    @ManyToOne
    @JoinColumn(name = "patient_user_id")
    private Patient patient;
}

package edu.t1.calculator.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private UUID paymentId;
    @Column(nullable = false)
    private Integer number;
    @Column(nullable = false)
    private LocalDateTime paymentDate;
    @Column(nullable = false)
    private Double monthlyPayment;
    @Column(nullable = false)
    private Double debtPayment;
    @Column(nullable = false)
    private Double interestPayment;
    @Column(nullable = false)
    private Double debtBalance;
    @ManyToOne
    @JoinColumn(name = "credit_id", nullable = false)
    private Credit credit;

    public Payment(Integer number,
                   LocalDateTime paymentDate,
                   Double monthlyPayment,
                   Double debtPayment,
                   Double interestPayment,
                   Double debtBalance,
                   Credit credit) {
        this.number = number;
        this.paymentDate = paymentDate;
        this.monthlyPayment = monthlyPayment;
        this.debtPayment = debtPayment;
        this.interestPayment = interestPayment;
        this.debtBalance = debtBalance;
        this.credit = credit;
    }
}

package edu.t1.calculator.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "credits")
@Getter
@Setter
@NoArgsConstructor
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private UUID creditId;
    @Column(nullable = false)
    private String ipAddress;
    @Column(nullable = false)
    private String paymentType;
    @Column(nullable = false)
    private Double amount;
    @Column(nullable = false)
    private Double rate;
    @Column(nullable = false)
    private Integer term;
    @Column(nullable = false)
    private LocalDateTime startDate;
    @OneToMany(mappedBy = "credit", cascade = CascadeType.ALL)
    private List<Payment> payments;

    public Credit(String ipAddress, String paymentType, Double amount, Double rate, Integer term, LocalDateTime startDate) {
        this.ipAddress = ipAddress;
        this.paymentType = paymentType;
        this.amount = amount;
        this.rate = rate;
        this.term = term;
        this.startDate = startDate;
    }
}

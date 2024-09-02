package edu.t1.calculator.repository;


import edu.t1.calculator.repository.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface CreditRepository extends JpaRepository<Credit, UUID> {
    Credit findByCreditId(UUID creditId);
}

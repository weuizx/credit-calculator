package edu.t1.calculator.controller.dto;

import java.time.LocalDateTime;

public record ScheduleItem(
        Integer number,
        LocalDateTime paymentDate,
        Double monthlyPayment,
        Double debtPayment,
        Double interestPayment,
        Double debtBalance
) {
}

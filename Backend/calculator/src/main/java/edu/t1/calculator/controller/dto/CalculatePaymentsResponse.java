package edu.t1.calculator.controller.dto;

import lombok.Data;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Data
public class CalculatePaymentsResponse {
    @Setter
    private UUID creditId;
    private Double initialMonthlyPayment;
    private Double finalMonthlyPayment;
    private Double totalPayment;
    private List<ScheduleItem> schedule;

    public CalculatePaymentsResponse(UUID creditId, Double initialMonthlyPayment, Double finalMonthlyPayment,
                                     Double totalPayment, List<ScheduleItem> schedule) {
        this.creditId = creditId;
        this.initialMonthlyPayment = initialMonthlyPayment;
        this.finalMonthlyPayment = finalMonthlyPayment;
        this.totalPayment = totalPayment;
        this.schedule = schedule;
    }
}

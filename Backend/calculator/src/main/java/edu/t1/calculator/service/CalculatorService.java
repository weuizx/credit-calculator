package edu.t1.calculator.service;

import edu.t1.calculator.controller.dto.CalculatePaymentsRequest;
import edu.t1.calculator.controller.dto.CalculatePaymentsResponse;
import edu.t1.calculator.controller.dto.ScheduleItem;

import java.util.List;
import java.util.UUID;

public interface CalculatorService {
    CalculatePaymentsResponse calculatePayments(String ipAddress, CalculatePaymentsRequest request);

    List<ScheduleItem> getSchedule(UUID creditId);
}
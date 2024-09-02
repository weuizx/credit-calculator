package edu.t1.calculator.controller.dto;


import edu.t1.calculator.controller.dto.validation.ValidData;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record CalculatePaymentsRequest(
        @NotNull(message = "paymentType must not be null")
        @Pattern(regexp = "^(ANNUITY|DIFFERENTIATED)$", message = "paymentType must be one of: ANNUITY, DIFFERENTIATED")
        String paymentType,
        @NotNull(message = "amount must not be null")
        @DecimalMin(value = "0.01", message = "amount must be greater than 0.01")
        @DecimalMax(value = "1000000000000.00", message = "amount must be less than 1000000000000.00")
        Double amount,
        @NotNull(message = "rate must not be null")
        @DecimalMin(value = "0.01", message = "rate must be greater than 0.01")
        @DecimalMax(value = "100.00", message = "rate must be less than 100.00")
        Double rate,
        @NotNull(message = "term must not be null")
        @Min(value = 1, message = "term must be greater than 1")
        @Max(value = 480, message = "term must be less than 480")
        Integer term,
        @NotNull(message = "startDate must not be null")
        @ValidData(min = "1600-01-01T00:00:00", max = "3000-01-01T23:59:59",
                message = "startDate must not be less than 1600-01-01 and must not be greater than 3000-01-01")
        LocalDateTime startDate
) {
}

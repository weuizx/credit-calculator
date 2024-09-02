package edu.t1.calculator.controller;

import edu.t1.calculator.controller.dto.CalculatePaymentsRequest;
import edu.t1.calculator.controller.dto.CalculatePaymentsResponse;
import edu.t1.calculator.controller.dto.ErrorResponse;
import edu.t1.calculator.service.CalculatorService;
import edu.t1.calculator.service.ScheduleService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Validated
@OpenAPIDefinition(
        info = @Info(
                title = "Calculator service API",
                description = "API для расчета графика платежей по кредиту"
        )
)
public class CalculatorServiceController {
    private final CalculatorService calculatorService;
    private final ScheduleService scheduleService;

    @PostMapping("/calculate-payments")
    @Operation(summary = "Получить график платежей по кредиту",
            description = "Возвращает json файл с общей информацией по кредиту и график платежей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение графика",
                         content = @Content(mediaType = "application/json",
                         schema = @Schema(implementation = CalculatePaymentsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос",
                         content = @Content(mediaType = "application/json",
                         schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CalculatePaymentsResponse> calculatePayments(HttpServletRequest request,
                                                                       @Parameter(description = "Параметры кредита", required = true) @Valid @RequestBody CalculatePaymentsRequest requestBody) {
        return ResponseEntity.ok(calculatorService.calculatePayments(
                request.getRemoteAddr(),
                requestBody
        ));
    }

    @GetMapping("/download-schedule")
    @Operation(summary = "Скачать график платежей по кредиту в формате xlsx",
            description = "Возвращает xlsx файл представляющий график платежей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение файла",
                         content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                         schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос",
                         content = @Content(mediaType = "application/json",
                         schema = @Schema(implementation = ErrorResponse.class)))
    })

    public ResponseEntity<Resource> getSchedule(
            @Parameter(description = "Идентификатор кредита в таблице базы данных", required = true)
            @Valid @RequestParam @NotNull(message = "creditId must not be null") UUID creditId) {
        Resource scheduleFile = scheduleService.generateScheduleFile(creditId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=payment_table.xlsx");
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return new ResponseEntity<>(scheduleFile, headers, HttpStatus.OK);
    }

}

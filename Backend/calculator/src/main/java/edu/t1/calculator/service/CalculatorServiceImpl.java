package edu.t1.calculator.service;

import edu.t1.calculator.controller.dto.CalculatePaymentsRequest;
import edu.t1.calculator.controller.dto.CalculatePaymentsResponse;
import edu.t1.calculator.controller.dto.ScheduleItem;
import edu.t1.calculator.repository.CreditRepository;
import edu.t1.calculator.repository.entity.Credit;
import edu.t1.calculator.repository.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalculatorServiceImpl implements CalculatorService {

    private final CreditRepository creditRepository;

    @Override
    public CalculatePaymentsResponse calculatePayments(String ipAddress, CalculatePaymentsRequest request) {


        Credit credit = new Credit(ipAddress, request.paymentType(), request.amount(), request.rate(),
                request.term(), request.startDate());
        List<Payment> payments = new LinkedList<>();

        CalculatePaymentsResponse response = null;
        if (request.paymentType().equals("ANNUITY")) {
            response = calculateAnnuityPayments(request.amount(), request.rate() / 100,
                    request.term(), request.startDate(), credit, payments);
        } else if (request.paymentType().equals("DIFFERENTIATED")) {
            response = calculateDifferentiatedPayments(request.amount(), request.rate() / 100,
                    request.term(), request.startDate(), credit, payments);
        }

        credit.setPayments(payments);
        UUID creditId = creditRepository.save(credit).getCreditId();
        response.setCreditId(creditId);

        return response;
    }

    private CalculatePaymentsResponse calculateDifferentiatedPayments(double amount, double decimalRate, int term,
                                                                      LocalDateTime startDate,
                                                                      Credit credit, List<Payment> payments) {
        double monthlyPayment;
        double totalPayment = 0;
        List<ScheduleItem> schedule = new LinkedList<>();

        int number = 0;
        LocalDateTime paymentDate = startDate;
        double debtPayment = round(amount / term);
        double interestPayment;
        double debtBalance = amount;

        for (int i = 0; i < term; i++) {
            number++;
            paymentDate = paymentDate.plusMonths(1);
            interestPayment = round(debtBalance * decimalRate / 12);
            monthlyPayment = round(debtPayment + interestPayment);
            debtBalance = round(debtBalance - debtPayment);
            totalPayment += monthlyPayment;

            schedule.add(new ScheduleItem(number, paymentDate, monthlyPayment,
                    debtPayment, interestPayment, debtBalance));
            payments.add(new Payment(number, paymentDate, monthlyPayment,
                    debtPayment, interestPayment, debtBalance, credit));
        }
        return new CalculatePaymentsResponse(null, schedule.getFirst().monthlyPayment(), schedule.getLast().monthlyPayment(),
                round(totalPayment), schedule);
    }

    private CalculatePaymentsResponse calculateAnnuityPayments(double amount, double decimalRate, int term,
                                                               LocalDateTime startDate,
                                                               Credit credit, List<Payment> payments) {

        double monthlyPayment = monthlyAnnuityPayment(amount, decimalRate, term);
        double totalPayment = round(monthlyPayment * term);
        List<ScheduleItem> schedule = new LinkedList<>();

        int number = 0;
        LocalDateTime paymentDate = startDate;
        double debtPayment;
        double interestPayment;
        double debtBalance = amount;

        for (int i = 0; i < term; i++) {
            number++;
            paymentDate = paymentDate.plusMonths(1);
            debtPayment = round(monthlyPayment - debtBalance * decimalRate / 12);
            interestPayment = round(monthlyPayment - debtPayment);
            debtBalance = round(debtBalance - debtPayment);

            schedule.add(new ScheduleItem(number, paymentDate, monthlyPayment,
                    debtPayment, interestPayment, debtBalance));
            payments.add(new Payment(number, paymentDate, monthlyPayment,
                    debtPayment, interestPayment, debtBalance, credit));
        }

        return new CalculatePaymentsResponse(null, monthlyPayment, monthlyPayment,
                totalPayment, schedule);
    }

    private double monthlyAnnuityPayment(double amount, double decimalRate, int term) {

        double monthlyPayment = amount * decimalRate / (1 - Math.pow((1 + decimalRate / 12), -term)) / 12;

        return Math.floor(monthlyPayment * 100.0) / 100.0;
    }

    private double round(double number) {
        return Math.round(number * 100.0) / 100.0;
    }

    @Override
    public List<ScheduleItem> getSchedule(UUID creditId) {
        Credit credit = creditRepository.findByCreditId(creditId);

        if (credit == null)
            return new LinkedList<>();

        return credit.getPayments().stream().map(payment ->
                new ScheduleItem(payment.getNumber(), payment.getPaymentDate(), payment.getMonthlyPayment(),
                        payment.getDebtPayment(), payment.getInterestPayment(), payment.getDebtBalance())).toList();
    }
}

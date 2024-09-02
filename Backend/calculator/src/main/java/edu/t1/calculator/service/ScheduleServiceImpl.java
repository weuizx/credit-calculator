package edu.t1.calculator.service;

import edu.t1.calculator.controller.exceptions.CreditNotFoundException;
import edu.t1.calculator.controller.exceptions.GenerateXlsxException;
import edu.t1.calculator.repository.CreditRepository;
import edu.t1.calculator.repository.entity.Credit;
import edu.t1.calculator.repository.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final CreditRepository creditRepository;

    @Override
    public ByteArrayResource generateScheduleFile(UUID creditId) {
        Credit credit = creditRepository.findByCreditId(creditId);
        if (credit == null) {
            throw new CreditNotFoundException("Credit not found");
        }
        List<Payment> payments = credit.getPayments();
        String[][] data = new String[payments.size() + 1][6];
        String[] line = {"Номер", "Дата", "Месячный платеж", "Платеж по долгу", "Платеж по процентам", "Остаток долга"};
        data[0] = line;

        for (int i = 0; i < payments.size(); i++) {
            Payment payment = payments.get(i);
            data[i + 1][0] = payment.getNumber().toString();
            data[i + 1][1] = payment.getPaymentDate().toString();
            data[i + 1][2] = payment.getMonthlyPayment().toString();
            data[i + 1][3] = payment.getDebtPayment().toString();
            data[i + 1][4] = payment.getInterestPayment().toString();
            data[i + 1][5] = payment.getDebtBalance().toString();
        }

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("График платежей");

            for (int i = 0; i < data.length; i++) {
                Row row = sheet.createRow(i);
                for (int j = 0; j < data[i].length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(data[i][j]);
                }
            }

            for (int i = 0; i < data[0].length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (IOException e) {
            throw new GenerateXlsxException("Exception during generating xlsx file");
        }
    }
}

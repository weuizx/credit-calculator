package edu.t1.calculator.service;

import edu.t1.calculator.controller.exceptions.GenerateXlsxException;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.util.UUID;

public interface ScheduleService {
    public ByteArrayResource generateScheduleFile(UUID creditId);
}

package com.neoflex.vacationcalculator.service.impl;

import com.neoflex.vacationcalculator.dto.request.VacationRequest;
import com.neoflex.vacationcalculator.dto.response.VacationResponse;
import com.neoflex.vacationcalculator.service.HolidayService;
import com.neoflex.vacationcalculator.service.VacationCalculatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacationCalculatorServiceImpl implements VacationCalculatorService {

    private static final BigDecimal AVERAGE_DAYS_IN_MONTH = BigDecimal.valueOf(29.3);
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final HolidayService holidayService;

    @Override
    public VacationResponse calculateVacationPay(VacationRequest request) {
        validateRequest(request);

        BigDecimal averageDailyEarnings = calculateAverageDailyEarnings(request.averageSalary());

        if (request.hasSpecificDates()) {
            return calculateWithDates(request, averageDailyEarnings);
        } else {
            return calculateSimple(request, averageDailyEarnings);
        }
    }

    private void validateRequest(VacationRequest request) {
        if (request.hasSpecificDates() && request.startDate().isAfter(request.endDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }

    private VacationResponse calculateSimple(VacationRequest request, BigDecimal averageDailyEarnings) {
        BigDecimal vacationPay = averageDailyEarnings
                .multiply(BigDecimal.valueOf(request.vacationDays()))
                .setScale(SCALE, ROUNDING);

        return new VacationResponse(vacationPay, null, null, null, averageDailyEarnings);
    }

    private VacationResponse calculateWithDates(VacationRequest request, BigDecimal averageDailyEarnings) {
        validateVacationPeriod(request);

        int workingDays = holidayService.countWorkingDays(request.startDate(), request.endDate());
        BigDecimal vacationPay = averageDailyEarnings
                .multiply(BigDecimal.valueOf(workingDays))
                .setScale(SCALE, ROUNDING);

        return new VacationResponse(
                vacationPay,
                workingDays,
                request.startDate(),
                request.endDate(),
                averageDailyEarnings
        );
    }

    private void validateVacationPeriod(VacationRequest request) {
        long actualDays = ChronoUnit.DAYS.between(request.startDate(), request.endDate()) + 1;
        if (actualDays != request.vacationDays()) {
            throw new IllegalArgumentException("Vacation period doesn't match specified days");
        }
    }

    private BigDecimal calculateAverageDailyEarnings(BigDecimal averageSalary) {
        return averageSalary.divide(AVERAGE_DAYS_IN_MONTH, SCALE, ROUNDING);
    }
}
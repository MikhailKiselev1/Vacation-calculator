package com.neoflex.vacationcalculator.service;

import com.neoflex.vacationcalculator.dto.request.VacationRequest;
import com.neoflex.vacationcalculator.dto.response.VacationResponse;
import com.neoflex.vacationcalculator.service.impl.VacationCalculatorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VacationCalculatorServiceImplTest {

    @Mock
    private HolidayService holidayService;

    private VacationCalculatorServiceImpl vacationCalculatorService;

    @BeforeEach
    void setUp() {
        vacationCalculatorService = new VacationCalculatorServiceImpl(holidayService);
    }

    @Test
    void calculateVacationPay_StartDateAfterEndDate_ThrowsException() {
        // Given
        VacationRequest request = new VacationRequest(
                new BigDecimal("50000"),
                14,
                LocalDate.of(2024, 7, 14),
                LocalDate.of(2024, 7, 1)
        );

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> vacationCalculatorService.calculateVacationPay(request)
        );

        assertEquals("Start date must be before end date", exception.getMessage());
    }

    @Test
    void calculateVacationPay_PeriodDoesNotMatchDays_ThrowsException() {
        // Given
        VacationRequest request = new VacationRequest(
                new BigDecimal("50000"),
                20, // 20 дней, а период только 14 дней
                LocalDate.of(2024, 7, 1),
                LocalDate.of(2024, 7, 14)
        );

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> vacationCalculatorService.calculateVacationPay(request)
        );

        assertEquals("Vacation period doesn't match specified days", exception.getMessage());
    }

    @Test
    void calculateVacationPay_SalaryZero_CalculatesCorrectly() {
        // Given
        VacationRequest request = new VacationRequest(
                BigDecimal.ZERO,
                14,
                null,
                null
        );

        // When
        VacationResponse response = vacationCalculatorService.calculateVacationPay(request);

        // Then
        assertNotNull(response);
        assertEquals(BigDecimal.ZERO.setScale(2), response.vacationPay());
        assertEquals(BigDecimal.ZERO.setScale(2), response.averageDailyEarnings());
    }


    @Test
    void calculateVacationPay_WithDatesButNoWorkingDays_ReturnsZeroPay() {
        // Given
        VacationRequest request = new VacationRequest(
                new BigDecimal("50000"),
                14,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 14)
        );

        when(holidayService.countWorkingDays(any(), any())).thenReturn(0);

        // When
        VacationResponse response = vacationCalculatorService.calculateVacationPay(request);

        // Then
        assertNotNull(response);
        assertEquals(BigDecimal.ZERO.setScale(2), response.vacationPay());
        assertEquals(0, response.workingDays());
    }

    @Test
    void calculateVacationPay_MinSalary_CalculatesCorrectly() {
        // Given
        VacationRequest request = new VacationRequest(
                new BigDecimal("0.01"), // Минимальная зарплата
                14,
                null,
                null
        );

        // When
        VacationResponse response = vacationCalculatorService.calculateVacationPay(request);

        // Then
        assertNotNull(response);
        assertEquals(new BigDecimal("0.00"), response.vacationPay()); // 0.01 / 29.3 * 14 = 0.0047 ≈ 0.00
        assertEquals(new BigDecimal("0.00"), response.averageDailyEarnings()); // 0.01 / 29.3 = 0.00034 ≈ 0.00
    }

}
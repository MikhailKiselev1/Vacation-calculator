package com.neoflex.vacationcalculator.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Запрос на расчет отпускных")
public record VacationRequest(
        @NotNull @Positive BigDecimal averageSalary,
        @NotNull @Min(1) @Max(365) Integer vacationDays,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate endDate
) {
    public boolean hasSpecificDates() {
        return startDate != null && endDate != null;
    }
}
package com.neoflex.vacationcalculator.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Ответ с расчетом отпускных")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record VacationResponse(
        BigDecimal vacationPay,
        Integer workingDays,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal averageDailyEarnings
) {}
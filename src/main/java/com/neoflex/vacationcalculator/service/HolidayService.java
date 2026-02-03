package com.neoflex.vacationcalculator.service;

import java.time.LocalDate;

public interface HolidayService {
    boolean isHoliday(LocalDate date);

    int countWorkingDays(LocalDate start, LocalDate end);
}

package com.neoflex.vacationcalculator.service.impl;

import com.neoflex.vacationcalculator.service.HolidayService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

@Service
public class HolidayServiceImpl implements HolidayService {

    private final Set<LocalDate> holidays = new HashSet<>();

    public HolidayServiceImpl() {
        initializeHolidays();
    }

    @Override
    public boolean isHoliday(LocalDate date) {
        return holidays.contains(date) ||
                date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    @Override
    public int countWorkingDays(LocalDate start, LocalDate end) {
        int count = 0;
        LocalDate current = start;

        while (!current.isAfter(end)) {
            if (!isHoliday(current)) {
                count++;
            }
            current = current.plusDays(1);
        }

        return count;
    }

    private void initializeHolidays() {
        // Новогодние каникулы
        addHolidayRange(Month.JANUARY, 1, 8);
        // Рождество
        addHoliday(Month.JANUARY, 7);
        // День защитника Отечества
        addHoliday(Month.FEBRUARY, 23);
        // Международный женский день
        addHoliday(Month.MARCH, 8);
        // Праздник Весны и Труда
        addHoliday(Month.MAY, 1);
        // День Победы
        addHoliday(Month.MAY, 9);
        // День России
        addHoliday(Month.JUNE, 12);
        // День народного единства
        addHoliday(Month.NOVEMBER, 4);
    }

    private void addHoliday(Month month, int day) {
        for (int year = 2020; year <= 2030; year++) {
            holidays.add(LocalDate.of(year, month, day));
        }
    }

    private void addHolidayRange(Month month, int startDay, int endDay) {
        for (int year = 2020; year <= 2030; year++) {
            for (int day = startDay; day <= endDay; day++) {
                holidays.add(LocalDate.of(year, month, day));
            }
        }
    }
}

package com.neoflex.vacationcalculator.service;

import com.neoflex.vacationcalculator.dto.request.VacationRequest;
import com.neoflex.vacationcalculator.dto.response.VacationResponse;

public interface VacationCalculatorService {
    VacationResponse calculateVacationPay(VacationRequest request);
}
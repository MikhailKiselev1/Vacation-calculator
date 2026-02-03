package com.neoflex.vacationcalculator.controller;

import com.neoflex.vacationcalculator.dto.request.VacationRequest;
import com.neoflex.vacationcalculator.dto.response.VacationResponse;
import com.neoflex.vacationcalculator.service.VacationCalculatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Vacation Calculator")
public class VacationController {

    private final VacationCalculatorService vacationCalculatorService;

    @GetMapping("/calculate")
    @Operation(summary = "Calculate vacation pay")
    @ResponseStatus(HttpStatus.OK)
    public VacationResponse calculate(@Valid @RequestBody VacationRequest request) {
        return vacationCalculatorService.calculateVacationPay(request);
    }
}
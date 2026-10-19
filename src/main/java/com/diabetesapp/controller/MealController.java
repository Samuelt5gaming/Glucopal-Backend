package com.diabetesapp.controller;

import com.diabetesapp.dto.MealDto;
import com.diabetesapp.security.FirebasePrincipal;
import com.diabetesapp.service.CurrentUserService;
import com.diabetesapp.service.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/me/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;
    private final CurrentUserService currentUser;

    @PostMapping
    public ResponseEntity<MealDto.Response> addMeal(
            @AuthenticationPrincipal FirebasePrincipal me,
            @Valid @RequestBody MealDto.Request request) {
        return ResponseEntity.ok(mealService.addMeal(currentUser.id(me), request));
    }

    @GetMapping
    public ResponseEntity<List<MealDto.Response>> getMeals(@AuthenticationPrincipal FirebasePrincipal me) {
        return ResponseEntity.ok(mealService.getAllMeals(currentUser.id(me)));
    }

    @DeleteMapping("/{mealId}")
    public ResponseEntity<Void> deleteMeal(
            @AuthenticationPrincipal FirebasePrincipal me,
            @PathVariable Long mealId) {
        mealService.deleteMeal(currentUser.id(me), mealId);
        return ResponseEntity.noContent().build();
    }
}
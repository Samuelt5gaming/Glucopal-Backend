package com.diabetesapp.service;

import com.diabetesapp.dto.MealDto;
import com.diabetesapp.model.Meal;
import com.diabetesapp.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;

    public List<MealDto.Response> getAllMeals(Long userId) {
        return mealRepository.findByUserIdOrderByEatenAtDesc(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public MealDto.Response addMeal(Long userId, MealDto.Request request) {
        Meal meal = Meal.builder()
                .userId(userId)
                .photoUrl(request.getPhotoUrl())
                .mealName(request.getMealName())
                .eatenAt(request.getEatenAt())
                .carbsG(request.getCarbsG())
                .proteinG(request.getProteinG())
                .vitaminsMg(request.getVitaminsMg())
                .calories(request.getCalories())
                .notes(request.getNotes())
                .aiSuggested(request.getAiSuggested())
                .build();
        return toResponse(mealRepository.save(meal));
    }

    public void deleteMeal(Long userId, Long mealId) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new IllegalArgumentException("Meal not found"));
        if (!meal.getUserId().equals(userId)) {
            throw new SecurityException("Access denied");
        }
        mealRepository.deleteById(mealId);
    }

    private MealDto.Response toResponse(Meal m) {
        return MealDto.Response.builder()
                .id(m.getId())
                .userId(m.getUserId())
                .photoUrl(m.getPhotoUrl())
                .mealName(m.getMealName())
                .eatenAt(m.getEatenAt())
                .carbsG(m.getCarbsG())
                .proteinG(m.getProteinG())
                .vitaminsMg(m.getVitaminsMg())
                .calories(m.getCalories())
                .notes(m.getNotes())
                .aiSuggested(m.getAiSuggested())
                .build();
    }
}
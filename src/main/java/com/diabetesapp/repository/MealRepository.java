package com.diabetesapp.repository;

import com.diabetesapp.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByUserIdOrderByEatenAtDesc(Long userId);
}